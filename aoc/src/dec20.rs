#[cfg(test)]
mod tests {
      use std::time::Instant;
      #[test]
    fn dec18_part1() {
      let lines: Vec<String> = include_str!("../inputs/dec20.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      assert!(620==super::solve_part1(&lines));
      super::solve_part2(&lines);
      println!("Complete in {:?}", start.elapsed());
      // part 2 7366
      // Complete in 1301.929033168s
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

///////////////////////////////////////
/// Donut
/// 
/// 
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


///////////////////////////////////////
/// Block
/// 
/// 
/// 
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
  // return format!("({},{},{}) {} ",b.position.0, b.position.1, b.level, b.label);
  if b.label.trim() != "".to_string() { 
  return format!("{},", b.label.trim());
  } else {return "".to_string(); }
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
            if block_copy.level.abs() > 7 { return None; }
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

fn navigate_to_portal(donut : &Donut, current_location : (usize,usize), block_history : &Vec<Block>, max_len : usize) -> Vec<Block> {
  let mut path : Vec<Block> = vec![];

  let last_level;
  if let Some(last_block) = block_history.last() {
    last_level = last_block.level;
  } else {
    last_level = 0;
  }

  let mut blocks = vec![];
  for portals_vectors in donut.portals.values() {
    for portal in portals_vectors {
      let d = match portal {
        Portal::Up(pos,label) => (*pos,label,simple_path(&donut, *pos, current_location, &vec![])),
        Portal::Down(pos,label) if last_level > 0 => (*pos,label,simple_path(&donut, *pos, current_location, &vec![])),
        Portal::Down(pos,label) if last_level == 0 => (*pos,label,None),
        _ => panic!("unreachable")
      };      
      if let Some(distance) = d.2 {
        if distance == 0 || block_history.iter().filter(|b| b.position.0 == d.0.0 && b.position.1 == d.0.1 && b.level == last_level).count() > 0 { break; }
        blocks.push(Block{ position : d.0, label : (*d.1).clone(), distance : distance, level : last_level, value : '.'});
      }
    }
  }

  let mut candidates = vec![];

  if let Some(distance) = simple_path(&donut, current_location, donut.end, &vec![]) {
    if last_level == 0 {
      let mut new_history = block_history.clone();
      println!("Found a solution [{}] history [{:?}", &new_history.iter().map(|b| b.distance).sum::<usize>(), block_history.iter().map(|b| format!("{}{}({})",b.label.to_string(),b.level,b.distance)).collect::<Vec<String>>().join(","));
      new_history.push(Block{ position : donut.end, label : "ZZ".to_string(), distance, level : 0, value : '.'});
      candidates.push(new_history);
    }
  }
  if block_history.iter().map(|b| b.label.to_string()).collect::<String>() == 
      "XFXFCKCKZHZHWBWBICICRFRFNMNMLPLPFDFDXQXQWBWBZHZHCKCKXFXFOAOACJCJREREICICRFRFNMNMLPLPFDFDXQXQWBWBZHZHCKCKXFXFOAOACJCJREREXQXQFDFD".to_string() {
    println!("Exploring [{}] history [{:?}", blocks.iter().map(|b| b.label.to_string()).collect::<String>(), block_history.iter().map(|b| format!("{}{}({})",b.label.to_string(),b.level,b.distance)).collect::<Vec<String>>().join(","));
  }
  // println!("{:?}", blocks);
  for block in blocks {
    let mut new_history = block_history.clone();
    let block_copy = block.clone();

    let portal_match = donut.get_portal_neighbors(&block_copy.position);
    new_history.push(block_copy);

    match &portal_match[0] {
      Portal::Up(location,label) => {new_history.push(Block{ position : *location, label : label.clone(), distance : 1, level : last_level-1, value : '.'})},
      Portal::Down(location,label) => {new_history.push(Block{ position : *location, label : label.clone(), distance : 1, level : last_level+1, value : '.'})},
      _ => {panic!("non portal"); }
    }
    if new_history.iter().map(|b| b.distance).sum::<usize>() < max_len {  
      let candidate = navigate_to_portal(&donut, new_history.last().unwrap().position, &new_history, max_len);
      if candidate.len() > 0 {
        candidates.push(candidate.clone());
      }
    }
  }

  while let Some(candidate) = candidates.pop() {
    if path.len() == 0 {
      path = candidate.clone();
    } else if candidate.iter().map(|b| b.distance).sum::<usize>() < path.iter().map(|b| b.distance).sum::<usize>() {
      path = candidate.clone();
    }
  }

  return path;
}

fn simple_path(donut : &Donut, 
  point_a : (usize,usize), 
  point_b : (usize,usize), 
  history : &Vec<(usize,usize)>) -> Option<usize> {
  let mut min_distance = 99999;

  if point_a == point_b { 
    return Some(history.len()); 
  }

  let neighbors = get_neighbors(&point_a);
  // println!("history {} exploring {:?}",history.iter().map(|p| block_format(p)).collect::<String>(), neighbors);
  for n in neighbors {
    if let Some(n_o) = donut.map.get(&n) {
      if !n_o.is_wall() {
        if !history.contains(&n) {
          let mut new_history = history.clone();
          new_history.push((n.0,n.1));
          if let Some(d) = simple_path(&donut, n, point_b, &new_history) {
            if d < min_distance {min_distance = d; }
          }
        }
      }
    }
  }
  if min_distance < 99999 {   
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
  
  // println!("{:?}", path(&donut, donut.start, donut.end,&vec![], false));

  if let Some(d) = path(&donut, donut.start, donut.end,&vec![], false) {
    return d;
  } else {
    return 0;
  }
}

#[allow(dead_code)]
pub fn solve_part2(lines : &Vec<String>) -> usize {

  let donut = load_donut(lines);
  
  print_map(&donut.map, donut.start);
  let mut max = 7500;
  loop { 
    let retval = navigate_to_portal(&donut, donut.start, &vec![],max);
    if retval.len() == 0 {
      max += 1000;
      println!("trying {}", max);
    } else {
      println!("path [{:?}]", retval.iter().map(|b| format!("{}{}({})",b.label.to_string(),b.level,b.distance)).collect::<Vec<String>>().join(","));

      println!("part 2 {:?}", retval.iter().map(|b| b.distance).sum::<usize>());
      return retval.iter().map(|b| b.distance).sum::<usize>();
    }
   }
}