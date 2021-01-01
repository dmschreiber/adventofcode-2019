#[cfg(test)]
mod tests {
      use std::time::Instant;
      #[test]
    fn dec18_part1() {
      let lines: Vec<String> = include_str!("../inputs/dec20.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      super::solve_part1(&lines);
      println!("Complete in {:?}", start.elapsed());

    }
}

use std::collections::HashMap;
use std::convert::TryInto;

#[derive(Debug,Clone,Copy,Eq,PartialEq,Hash)]
pub enum Portal {
  Up((usize,usize)),
  Down((usize,usize)),
}

#[derive(Debug,Clone,Copy,Eq,PartialEq,Hash)]
pub struct Block {
  position : (usize,usize),
  value : char,
  distance : usize,
}

pub struct Donut {
  map : HashMap<(usize,usize),Block>,
  start : (usize,usize),
  end : (usize,usize),
  portals : HashMap<String,Vec<Portal>>,
}

impl Donut {
  fn get_portal_neighbors(&self, position : &(usize,usize)) -> Vec<(usize,usize)> {
    for p in self.portals.values() {
      if p.contains(&Portal::Up(*position)) {
        let my_point= p.iter().filter(|each_point| **each_point != Portal::Up(*position)).map(|each_point| {if let Portal::Down(p) = *each_point { p } else {panic!("unreachable")} }).collect::<Vec<(usize,usize)>>();
        return my_point;
      }
      if p.contains(&Portal::Down(*position)) {
        let my_point= p.iter().filter(|each_point| **each_point != Portal::Down(*position)).map(|each_point| {if let Portal::Up(p) = *each_point { p } else {panic!("unreachable")}}).collect::<Vec<(usize,usize)>>();
        return my_point;
      }
    }
    return vec![];
  }

  fn get_neighbors(&self, position : &(usize,usize)) -> Vec<(usize,usize)> {
    let mut all_neighbors = get_neighbors(position).iter().filter(|p| self.map.get(&p) != None && !self.map.get(&p).unwrap().is_wall()).map(|p| *p).collect::<Vec<(usize,usize)>>();

    all_neighbors.append(&mut self.get_portal_neighbors(position));
    return all_neighbors;
  }
}

fn get_neighbors(position : &(usize,usize)) -> Vec<(usize,usize)> {
  let mut neighbors = vec![];
  // if position.0 == 0 || position.1 == 0 { panic!("Going off the map"); }
  neighbors.push((position.0,position.1+1));
  neighbors.push((position.0+1,position.1));
  if position.0 > 0 {
    neighbors.push(((position.0 as i32 - 1).try_into().unwrap(), position.1));
  }
  if position.1 > 0 {
    neighbors.push((position.0, (position.1 as i32 -1).try_into().unwrap()));
  }
  return neighbors;
}
impl Block {
  #[allow(dead_code)]
  fn get_neighbors(&self) -> Vec<(usize,usize)> {
    return get_neighbors(&self.position);
  }

  #[allow(dead_code)]
  fn is_wall(&self) -> bool {

    if self.value == '#' { return true; }
    if self.value == ' ' { return true; }
    if self.value == '.' { return false; }
    return false;
  }
  #[allow(dead_code)]
  fn distance(&self, position : (usize,usize)) -> usize {
    return (position.0 as i32- self.position.0 as i32).abs() as usize + (position.1 as i32- self.position.1 as i32).abs() as usize;
  }
}

fn path(donut : &Donut, 
        point_a : (usize,usize), 
        point_b : (usize,usize), 
        history : &Vec<(usize,usize)>) -> Option<usize> {
  let mut min_distance = 9999;
  
  if point_a == point_b { return Some(history.len()); }

  let neighbors = donut.get_neighbors(&point_a);

    for n in neighbors {
      if n == point_b {
        if history.len()+1 < min_distance {min_distance = history.len()+1; break; }
      } 
      if let Some(n_o) = donut.map.get(&n) {
        if !n_o.is_wall() {
          if !history.contains(&n) {
            let mut new_history = history.clone();
            new_history.push(n);
            if let Some(d) = path(&donut, n, point_b, &new_history) {
              if d < min_distance {min_distance = d; }
            }
          }
        }
      }
    }
    if min_distance < 9999 { return Some(min_distance) };

    return None;
}

fn print_map(map : &HashMap<(usize,usize),Block>, current : (usize,usize)) {
  let max_row = map.keys().map(|(a,_b)| *a).max().unwrap();
  let max_col = map.keys().map(|(_a,b)| *b).max().unwrap();
  let min_row = map.keys().map(|(a,_b)| *a).min().unwrap();
  let min_col = map.keys().map(|(_a,b)| *b).min().unwrap();

  println!(" ---- ");
  for r in min_row..=max_row {
    for c in min_col..=max_col {
      if (r,c) == current {
        print!("@");
      } else {
        if let Some(b) = map.get(&(r,c)) {
          print!("{}", b.value);
        } else {
          print!(" ");
        }
      }
    }
    println!();

  }
}


fn quick_get(lines : &Vec<String>, r : usize, c : usize) -> char {
  if r>=lines.len() {return ' ';}
  if c>=lines[0].len() {return ' ';}
  return lines[r].as_bytes()[c] as char;
}

#[allow(dead_code)]
pub fn solve_part1(lines : &Vec<String>) -> usize {

  let mut map = HashMap::new();
  let mut start = (0,0);
  let mut end = (0,0);
  let mut portals : HashMap<String,Vec<Portal>>= HashMap::new();

  let max_row = lines.len();
  let max_col = lines[0].as_bytes().len();
  let mut row = 0;
  let mut col = 0;
  let mut current_position = (0,0);

  for r in 0..max_row {
    for c in 0..max_col {
      let spot = quick_get(&lines, r, c);
      let mut portal_key : String = "".to_string();
      let mut portal_point : Portal = Portal::Up((0,0));
      if spot >= 'A' && spot <= 'Z' {
        let next = quick_get(&lines,r,c+1);
        if next >= 'A' && next <= 'Z' {
          portal_key = format!("{}{}",spot,next);

          if quick_get(&lines,r,c+2) == '.' {   
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {             
              portal_point = Portal::Down((r-2,c));    
            } else {
              portal_point = Portal::Up((r-2,c));    

            }
          } else if quick_get(&lines,r,c-1) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {             
              portal_point = Portal::Down((r-2,c-3));
            } else {
              portal_point = Portal::Up((r-2,c-3));
            }
          }

        }

        let next = quick_get(&lines,r+1,c);
        if next >= 'A' && next <= 'Z' {
          portal_key = format!("{}{}",spot,next);
          if quick_get(&lines,r+2,c) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {
              portal_point = Portal::Down((r,c-2));

            } else {
              portal_point = Portal::Up((r,c-2));
            }
          } else if quick_get(&lines,r-1,c) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {
              portal_point = Portal::Down((r-3,c-2));
            } else {
              portal_point = Portal::Up((r-3,c-2));
            }
          }
        }

        if portal_key == "AA" {
          start = match portal_point {
            Portal::Up(point) => point,
            Portal::Down(point) => point
          };
        } else if portal_key == "ZZ" {
          end = match portal_point {
            Portal::Up(point) => point,
            Portal::Down(point) => point
          };
        }else if portal_key != "" {
          if let Some(p) = portals.get_mut(&portal_key.to_string()) {
            p.push(portal_point);          
          } else {
            portals.insert(portal_key.to_string(),vec![portal_point]);
          }
        }
      }
    }
  }

  println!("{:?}", portals);

  for (row,line) in lines[2..=max_row-2].iter().enumerate() {
    for (col,block) in line.as_bytes()[2..=max_col-2].iter().enumerate() {
      if *block == b'#' || *block == b'.' {
        map.insert((row,col),Block{ position : (row,col), value : *block as char, distance : 0});
      }
    }
  }
  let donut = Donut { map : map, start : start, end : end, portals : portals };
  println!("{:?}", donut.get_neighbors(&(4,7)));
  print_map(&donut.map, start);
  println!("{:?}", path(&donut, start, end,&vec![]));
  println!("{:?}", donut.get_neighbors(&(4,7)));
  return 0;
}