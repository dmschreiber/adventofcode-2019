#[cfg(test)]
mod tests {
      use std::time::Instant;
      #[test]
    fn dec18_part1() {
      let lines: Vec<String> = include_str!("../inputs/dec20.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      super::solve_part1(&lines);
      // super::solve_part2(&lines);
      println!("Complete in {:?}", start.elapsed());

    }
}

use std::collections::HashMap;
use std::convert::TryInto;

#[derive(Debug,Clone,Eq,PartialEq,Hash)]
pub enum Portal {
  Same((usize,usize)),
  Up((usize,usize),String),
  Down((usize,usize),String),
}

#[derive(Debug,Clone,Eq,PartialEq,Hash)]
pub struct Block {
  position : (usize,usize),
  value : char,
  distance : usize,
  level : i32,
  label : String,
}

pub struct Donut {
  map : HashMap<(usize,usize),Block>,
  start : (usize,usize),
  end : (usize,usize),
  portals : HashMap<String,Vec<Portal>>,
}

impl Donut {
  fn get_portal_neighbors(&self, position : &(usize,usize)) -> Vec<Portal> {
    for p in self.portals.values() {
      if p.iter().filter(|each_portal| enum_unwrap(*each_portal) == *position).count() > 0 {
      // if p.contains(&Portal::Up(*position)) {
        let my_point= p.iter().filter(|each_point| enum_unwrap(*each_point) != *position).map(|each_point| each_point.clone()).collect::<Vec<Portal>>();
        return my_point;
      }
    }
    return vec![];
  }

  fn get_neighbors(&self, position : &(usize,usize)) -> Vec<Portal> {
    let mut all_neighbors = get_neighbors(position).iter().filter(|p| self.map.get(&p) != None && !self.map.get(&p).unwrap().is_wall()).map(|p| Portal::Same(*p)).collect::<Vec<Portal>>();

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

fn net_levels(v : &Vec<Portal>) -> i32 {
  let mut net_levels = 0;
  for p in v {
    net_levels = net_levels + match p {
      Portal::Same(_n) => 0,
      Portal::Up(_n,_label)  => -1,
      Portal::Down(_n,_label) => 1
    }
  }
  return net_levels;
}

fn enum_unwrap(p : &Portal) -> (usize,usize) {
  match p {
    Portal::Same(result) => *result,
    Portal::Up(result,_label)  => *result,
    Portal::Down(result,_label) => *result
  }
}

fn block_format(b : &Block) -> String {
  return format!("({},{},{}) {} ",b.position.0, b.position.1, b.level, b.label);

}

fn enum_unwrap_format(p : &Portal) -> String {
  match p {
    Portal::Same(result) => format!(""),
    Portal::Up(result,label)  => format!("Up[{}] ",label),
    Portal::Down(result,label) => format!("Down[{}] ",label),
  }
}

fn path(donut : &Donut, 
        point_a : (usize,usize), 
        point_b : (usize,usize), 
        history : &Vec<Block>, three_d : bool) -> Option<usize> {
  let mut min_distance = 9999;
  
  if point_a == point_b { 
    if three_d && history.last().unwrap().level == 0 { 
      println!("{:?} Net levels {}", history.iter().map(|b| block_format(b)).collect::<String>(),history.last().unwrap().level); 
      return Some(history.len()); 
    }
    else if !three_d { return Some(history.len()); }
  }

  let neighbors = donut.get_neighbors(&point_a);
  // println!("history {} exploring {:?}",history.iter().map(|p| block_format(p)).collect::<String>(), neighbors);
    for n in neighbors {
      if let Some(n_o) = donut.map.get(&enum_unwrap(&n)) {
        if !n_o.is_wall() {
          let mut block_copy = n_o.clone();
          match &n {
            Portal::Same(result) => { if let Some(b) = history.last() { block_copy.level = b.level;} else {block_copy.level = 0} }
            Portal::Up(result,label) => { block_copy.label = label.to_string(); if let Some(b) = history.last() { block_copy.level = b.level-1;} else {block_copy.level = 0} }
            Portal::Down(result,label) => { block_copy.label = label.to_string(); if let Some(b) = history.last() { block_copy.level = b.level+1;} else {block_copy.level = 0} }
          }
          if history.iter().filter(|b| b.position.0 == n_o.position.0 && b.position.1 == n_o.position.1 && (!three_d || b.level == block_copy.level)).count() == 0 {
            let mut new_history = history.clone();
            if block_copy.level.abs() > 11 { return None; }
            new_history.push(block_copy);
            if let Some(d) = path(&donut, enum_unwrap(&n), point_b, &new_history, three_d) {
              if d < min_distance {min_distance = d; }
            }
          }
        }
      }
    }
    if min_distance < 9999 {   
      return Some(min_distance) ;
    }

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

fn load_donut(lines : &Vec<String>) -> Donut {
  let mut map = HashMap::new();
  let mut start = (0,0);
  let mut end = (0,0);
  let mut portals : HashMap<String,Vec<Portal>>= HashMap::new();

  let max_row = lines.len();
  let max_col = lines[0].as_bytes().len();

  for r in 0..max_row {
    for c in 0..max_col {
      let spot = quick_get(&lines, r, c);
      let mut portal_key : String = "".to_string();
      let mut portal_point : Portal = Portal::Up((0,0),"".to_string());
      if spot >= 'A' && spot <= 'Z' {
        let next = quick_get(&lines,r,c+1);
        if next >= 'A' && next <= 'Z' {
          portal_key = format!("{}{}",spot,next);

          if quick_get(&lines,r,c+2) == '.' {   
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {             
              portal_point = Portal::Down((r-2,c),portal_key.to_string());    
            } else {
              portal_point = Portal::Up((r-2,c),portal_key.to_string());    

            }
          } else if quick_get(&lines,r,c-1) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {             
              portal_point = Portal::Down((r-2,c-3),portal_key.to_string());
            } else {
              portal_point = Portal::Up((r-2,c-3),portal_key.to_string());
            }
          }

        }

        let next = quick_get(&lines,r+1,c);
        if next >= 'A' && next <= 'Z' {
          portal_key = format!("{}{}",spot,next);
          if quick_get(&lines,r+2,c) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {
              portal_point = Portal::Down((r,c-2),portal_key.to_string());

            } else {
              portal_point = Portal::Up((r,c-2),portal_key.to_string());
            }
          } else if quick_get(&lines,r-1,c) == '.' {
            if r<2 || r >=max_row-2 || c<2 || c >=max_col-2 {
              portal_point = Portal::Down((r-3,c-2),portal_key.to_string());
            } else {
              portal_point = Portal::Up((r-3,c-2),portal_key.to_string());
            }
          }
        }

        if portal_key == "AA" {
          start = enum_unwrap(&portal_point);
        } else if portal_key == "ZZ" {
          end = enum_unwrap(&portal_point);
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

  // println!("{:?}", portals);

  for (row,line) in lines[2..=max_row-2].iter().enumerate() {
    for (col,block) in line.as_bytes()[2..=max_col-2].iter().enumerate() {
      if *block == b'#' || *block == b'.' {
        map.insert((row,col),Block{ position : (row,col), value : *block as char, distance : 0, level : 0, label : "".to_string() });
      }
    }
  }
  let donut = Donut { map : map, start : start, end : end, portals : portals };
  return donut;
}

#[allow(dead_code)]
pub fn solve_part1(lines : &Vec<String>) -> usize {

  let donut = load_donut(lines);
  
  print_map(&donut.map, donut.start);
  println!("{:?}", path(&donut, donut.start, donut.end,&vec![], false));

  return 0;
}

#[allow(dead_code)]
pub fn solve_part2(lines : &Vec<String>) -> usize {

  let donut = load_donut(lines);
  
  print_map(&donut.map, donut.start);
  println!("part 2 {:?}", path(&donut, donut.start, donut.end,&vec![], true));

  return 0;
}