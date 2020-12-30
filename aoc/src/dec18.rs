#[cfg(test)]
mod tests {
    #[test]
    fn dec18_part1() {
      let lines: Vec<String> = include_str!("../inputs/dec18.txt").lines().map(|s| (&*s).to_string() ).collect();
      super::solve_part1(lines);
    }
}

use std::collections::HashMap;
use std::convert::TryInto;

#[derive(Debug,Clone,Copy)]
pub struct Block {
  position : (usize,usize),
  value : char,
  distance : usize,
}

impl Block {
  fn get_neighbors(&self) -> Vec<(usize,usize)> {
    let v : Vec<(i32,i32)> = vec![(-1,0), (0, -1), (0,1), (1,0)];
    let mut neighbors = vec![];

    for n in v {
      if (self.position.0 > 0 || n.0 >= 0) & (self.position.1 > 0 || n.1 >= 0) {
        let new_row : i32 = self.position.0 as i32 + n.0;
        let new_col : i32 = self.position.1 as i32 + n.1;
          neighbors.push((new_row.try_into().unwrap(),new_col.try_into().unwrap()));

      }
    }

    neighbors
  }

  fn is_key(&self) -> bool {
    return  self.value >= 'a' && self.value <= 'z';

  }

  fn is_wall(&self) -> bool {
    return self.value == '#' || (self.value >= 'A' && self.value <= 'Z');
  }
}

fn path(map : &HashMap<(usize,usize),Block>, point_a : (usize,usize), point_b : (usize,usize), history : Vec<(usize,usize)>) -> Option<usize> {
  let mut min_distance = 9999;

  if point_a == point_b { return Some(history.len()); }
  if let Some(b) = map.get(&point_a) {
    for n in b.get_neighbors() {
      if !history.contains(&n) {
        if let Some(n_o) = map.get(&n) {
          if !n_o.is_wall() {
            // println!("Calling path on {:?} seeking {:?} history {:?}", n, point_b, history);
            let mut new_history = history.clone();
            new_history.push(n);
            if let Some(d) = path(&map, n, point_b, new_history) {
              if d < min_distance { min_distance = d; }
            }
          }
        }
      }
    }
    if min_distance < 9999 { return Some(min_distance) };
  }
  return None;
}

fn print_map(map : &HashMap<(usize,usize),Block>) {
  let max_row = map.keys().map(|(a,_b)| *a).max().unwrap();
  let max_col = map.keys().map(|(_a,b)| *b).max().unwrap();
  let min_row = map.keys().map(|(a,_b)| *a).min().unwrap();
  let min_col = map.keys().map(|(_a,b)| *b).min().unwrap();

  println!(" ---- ");
  for r in min_row..=max_row {
    for c in min_col..=max_col {
      if let Some(b) = map.get(&(r,c)) {
        print!("{}", b.value);
      } else {
        print!(" ");
      }
    }
    println!();

  }
}

fn move_to_key(map : &HashMap<(usize,usize),Block>, current_position : (usize, usize), history : Vec<Block>) -> Vec<(char,usize)> {
  if map.values().filter(|b| b.is_key()).count() == 0 {
    // print_map(&map);
    let retval = history.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
    // println!("retval is {} {:?}", retval.iter().map(|(c,d)| d).sum::<usize>(), retval);
    return retval;
  }

  let mut v = vec![('0',99999)];
  for block in map.values()
      .filter(|b| b.is_key())
      .filter(|b| path(&map, current_position, b.position, vec![]) != None) {

    let new_position = block.position;
    if let Some(d) = path(&map, current_position, block.position, vec![]) {
      let mut new_history = history.clone();
      let mut block_copy = block.clone();
      block_copy.distance = d;
      new_history.push(block_copy);
      // println!("{:?} to {} is {:?}", current_position, block.value, d);
      let mut new_map = map.clone();
      let door_vec = new_map.values().filter(|b| b.value == ('A' as u8 + block.value as u8 -b'a') as char).map(|b| b.position ).collect::<Vec<(usize,usize)>>();
      if door_vec.len() > 0 {
        new_map.get_mut(&door_vec[0]).unwrap().value = '.' // remove door
      } 
      new_map.get_mut(&new_position).unwrap().value = '.'; // remove key

      let v_candidate= move_to_key(&new_map, new_position, new_history);
      
      if v_candidate.iter().map(|(c,d)| d).sum::<usize>() < v.iter().map(|(c,d)| d).sum::<usize>() {
        println!("Found a new low {:?} {:?}",  v_candidate.iter().map(|(c,d)| d).sum::<usize>(), v_candidate.iter().map(|(c,d)| *c).collect::<Vec<char>>());
        v = v_candidate;
      }
    }
        
  }
  return v;
}

pub fn solve_part1(lines : Vec<String>) {

  let mut map = HashMap::new();
  let mut row = 0;
  let mut col = 0;
  let mut current_position = (0,0);

  for line in lines {
    for block in line.as_bytes() {
      if *block == b'@' {
        current_position = (row,col);
        map.insert((row,col),Block{ position : (row,col), value : '.' as char, distance : 0});
      } else {
        map.insert((row,col),Block{ position : (row,col), value : *block as char, distance : 0});
      }
      col = col + 1;
    }
    col = 0;
    row = row + 1;
  }

  print_map(&map);
  println!("{:?}", move_to_key(&map, current_position, vec![]));
}