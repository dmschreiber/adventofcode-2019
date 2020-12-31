#[cfg(test)]
mod tests {
      use std::time::Instant;
      #[test]
    fn dec18_part1() {
      let lines: Vec<String> = include_str!("../inputs/dec18.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      // assert!(6316==super::solve_part1(lines));
      super::solve_part2(lines);
      println!("Complete in {:?}", start.elapsed());
//        Complete in 800.48883394s
//        Complete in 787.981406902s
//        Complete in 64.339802425s
//        Complete in 46.228593998s

    }
}

use std::time::Instant;
use std::collections::HashMap;
use std::convert::TryInto;
// use rayon::prelude::*;

#[derive(Debug,Clone,Copy)]
pub struct Block {
  position : (usize,usize),
  value : char,
  distance : usize,
}

fn get_neighbors(position : &(usize,usize)) -> Vec<(usize,usize)> {
  if position.0 == 0 || position.1 == 0 { panic!("Going off the map"); }
  return vec![(position.0,position.1+1), 
            (position.0+1,position.1),
            ((position.0 as i32 - 1).try_into().unwrap(), position.1),
            (position.0, (position.1 as i32 -1).try_into().unwrap())];

}
impl Block {
  #[allow(dead_code)]
  fn get_neighbors(&self) -> Vec<(usize,usize)> {
    return get_neighbors(&self.position);
  }

  #[allow(dead_code)]
  fn is_key_str(&self, history : &Vec<char>) -> bool {
    if  self.value >= 'a' && self.value <= 'z' {

      let is_key = history.iter().filter(|b| **b == self.value).count() == 0;
      return is_key;
    }
    return  false;

  }
  fn is_wall_str(&self, history : &Vec<char>) -> bool {
    if self.value == '#' { return true; }
    if self.value == '.' { return false; }
    if self.value >= 'A' && self.value <= 'Z' {
      let associated_key = (self.value as u8 - b'A' + b'a') as char;
      let is_wall = history.iter().filter(|b| **b == associated_key).count() == 0;

      return is_wall; // if my wall has a key in history, not a wall
    }
    return false;
  }

  #[allow(dead_code)]
  fn is_key(&self, history : &Vec<Block>) -> bool {
    if  self.value >= 'a' && self.value <= 'z' {
      let is_key = history.iter().filter(|b| b.value == self.value).count() == 0;
      return is_key;
    }
    return  false;

  }
  #[allow(dead_code)]
  fn is_wall(&self, history : &Vec<Block>) -> bool {

    if self.value == '#' { return true; }
    if self.value == '.' { return false; }
    if self.value >= 'A' && self.value <= 'Z' {
      let is_wall = history.iter().filter(|b| b.value == ('a' as u8 + self.value as u8 - b'A') as char).count() == 0;
      return is_wall; // if my wall has a key in history, not a wall
    }
    return false;
  }
  #[allow(dead_code)]
  fn distance(&self, position : (usize,usize)) -> usize {
    return (position.0 as i32- self.position.0 as i32).abs() as usize + (position.1 as i32- self.position.1 as i32).abs() as usize;
  }
}
#[allow(dead_code)]
fn path(map : &HashMap<(usize,usize),Block>, point_a : (usize,usize), point_b : (usize,usize), history : &Vec<(usize,usize)>, key_history : &Vec<char>) -> Option<usize> {
  let mut min_distance = 9999;
  let key_str = key_history;
  
  if point_a == point_b { return Some(history.len()); }

  let neighbors = get_neighbors(&point_a);

    for n in neighbors {
      if n == point_b {
        if history.len()+1 < min_distance {min_distance = history.len()+1; break; }
      } 
      if let Some(n_o) = map.get(&n) {
        if !n_o.is_wall_str(&key_str) && !n_o.is_key_str(&key_str) {
          if !history.contains(&n) {
            let mut new_history = history.clone();
            new_history.push(n);
            if let Some(d) = path(&map, n, point_b, &new_history, &key_history) {
              if d < min_distance {min_distance = d; }
            }
          }
        }
      }
    }
    if min_distance < 9999 { return Some(min_distance) };

    return None;
}

#[allow(dead_code)]
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

#[allow(dead_code)]
fn print_map_2(map : &HashMap<(usize,usize),Block>, currents : &Vec<(usize,usize)>) {
  let max_row = map.keys().map(|(a,_b)| *a).max().unwrap();
  let max_col = map.keys().map(|(_a,b)| *b).max().unwrap();
  let min_row = map.keys().map(|(a,_b)| *a).min().unwrap();
  let min_col = map.keys().map(|(_a,b)| *b).min().unwrap();

  println!(" ---- ");
  for r in min_row..=max_row {
    for c in min_col..=max_col {
      if currents.contains(&(r,c)) {
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

#[allow(dead_code)]
fn move_to_key(map : &HashMap<(usize,usize),Block>, current_position : (usize, usize), history : &Vec<Block>, cache : &mut HashMap<String,Vec<(char,usize)>>) -> Vec<(char,usize)> {
 
  if map.values().filter(|b| b.is_key(&history)).count() == 0 {
    let retval = history.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
    return retval;
  }

  let mut candidate_list : Vec<Vec<(char,usize)>> = vec![];
  let start = Instant::now();
  let key_history = history.iter().map(|b| b.value).collect::<Vec<char>>();
  let blocks = map.values()
                .filter(|b| b.is_key_str(&key_history))
                .map(|b| (b,path(&map, b.position, current_position, &vec![], &key_history)))
                .filter(|(_b,d)| *d != None)
                .map(|(b,d)| Block{ position : b.position, distance : d.unwrap(), value : b.value})
                .collect::<Vec<Block>>();

  if false {
    for _i in 0..history.len() { print!(">"); }
    println!("History {:?} Exploring {:?} options ({} keys left)", 
      history.iter().map(|b| b.value).collect::<Vec<char>>(), 
      blocks.iter().map(|b| b.value).collect::<Vec<char>>(),
      map.values().filter(|b| b.is_key(&history)).count());
  }

  for (_i,block) in blocks.iter().enumerate() {
    let mut new_history = history.clone();
    let block_copy = block.clone().to_owned();
    new_history.push(block_copy);

    let v_candidate : Vec<(char,usize)>;
    // let start = Instant::now();
    let key = format!("{}-{}-{}", block.position.0, block.position.1, map.values().filter(|b| b.is_key(&history)).map(|b| b.value).collect::<String>());

    if let Some(temp) = cache.get(&key) {
      let mut front = new_history.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
      let mut back = temp.clone();
      front.append(&mut back);
      v_candidate = front;

    } else {
      v_candidate= move_to_key(&map, block.position, &new_history, cache);
      cache.insert(key,v_candidate.clone()[history.len()+1..].to_vec());
    }

    candidate_list.push(v_candidate);
    candidate_list.dedup();
  }

  // check my list of possible paths
  if candidate_list.len() == 0 { return vec![]; }

  let mut minimum = 9999; // candidate_list[0].iter().map(|(_c,distance)| distance).sum::<usize>();
  let mut smallest = vec![]; // candidate_list[0].clone();

  while let Some(a) = candidate_list.pop() {
    let my_size = a.iter().map(|(_c,distance)| distance).sum::<usize>();
    if  my_size < minimum {
      minimum = my_size ;
      smallest = a.clone();
    }  
  }
  return smallest

} 

#[allow(dead_code)]
pub fn solve_part1(lines : Vec<String>) -> usize {

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

  print_map(&map, current_position);

  // let f_position = map.values().filter(|b| b.value == 'f').map(|b| b.position).collect::<Vec<(usize,usize)>>()[0];
  // let start = Instant::now();
  // println!("shortest path {:?}", path(&map, current_position, f_position, &vec![], &vec![]));
  // println!("path time {:?}", start.elapsed());
  // let start = Instant::now();
  // println!("shortest path {:?}", path(&map, f_position, current_position, &vec![], &vec![]));
  // println!("path time {:?}", start.elapsed());
  // return 0;
  let retval = move_to_key(&map, current_position, &vec![], &mut HashMap::new());
  println!("{:?}", retval);
  println!("{}", retval.iter().map(|(_a,b)| b).sum::<usize>());
  retval.iter().map(|(_a,b)| b).sum::<usize>()
}

fn create_complex_key_history(history : &Vec<Vec<Block>>) -> Vec<char> {
  let mut complete_history = vec![];
  for h in history {
    let mut h_flat = h.iter().map(|b| b.value).collect::<Vec<char>>();
    complete_history.append(&mut h_flat);
  }
  return complete_history;
}

#[allow(dead_code)]
fn move_to_key_2(map : &HashMap<(usize,usize),Block>, current_positions : &Vec<(usize, usize)>, history : &Vec<Vec<Block>>, cache : &mut HashMap<String,Vec<Vec<(char,usize)>>>) -> Vec<Vec<(char,usize)>> {
  let mut candidate_list = vec![];
  let key_history = create_complex_key_history(history);

  if map.values().filter(|b| b.is_key_str(&key_history)).count() == 0 {
    let retval = history.iter().map(|v| v.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>()).collect::<Vec<Vec<(char,usize)>>>(); // fold(vec![], |acc, v| {acc.append(&mut v); acc});
    return retval;
  }

  for (which_robot,current_position) in current_positions.iter().enumerate() {
    let blocks = map.values()
                  .filter(|b| b.is_key_str(&key_history))
                  .map(|b| (b,path(&map, b.position, *current_position, &vec![], &key_history)))
                  .filter(|(_b,d)| *d != None)
                  .map(|(b,d)| Block{ position : b.position, distance : d.unwrap(), value : b.value})
                  .collect::<Vec<Block>>();


    if true {
      print!("{} {:?} ", which_robot, current_position);
      if history.len() > 0 {
        for _i in 0..history[which_robot].len() { print!(">"); }
        print!("History {:?}", history[which_robot].iter().map(|b| b.value).collect::<Vec<char>>());
      }
      println!("Exploring {:?} options ({} keys left)", 
        blocks.iter().map(|b| b.value).collect::<Vec<char>>(),
        map.values().filter(|b| b.is_key_str(&key_history)).count());
    }

    for block in blocks {
      let mut new_history : Vec<Vec<Block>> = vec![];
      if history.len() == 0 {
        for _i in current_positions {
          new_history.push(vec![]);
        }
      } else {
        new_history = history.clone();
      }
    
      let mut new_positions = current_positions.clone();
      new_positions[which_robot] = block.position;
      new_history[which_robot].push(block.clone());

      let candidate;
      let key = format!("{:?}-{}", new_positions, map.values().filter(|b| b.is_key_str(&key_history)).map(|b| b.value).collect::<String>());
      if let Some(temp) = cache.get(&key) {
        // println!("Found key {} {:?}", key, temp);

        let mut cached_candidate = vec![vec![],vec![],vec![],vec![]];

        for (which_history,cached_answer) in temp.iter().enumerate() {
          cached_candidate[which_history] = new_history[which_history].iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
          cached_candidate[which_history].append(&mut cached_answer.clone());
        }
        candidate = cached_candidate;
  
      } else {
        candidate = move_to_key_2(&map, &new_positions, &new_history, cache);
        // println!("Storing key {} {:?}", key, candidate);
        let mut cached_candidate = vec![vec![],vec![],vec![],vec![]];

        for (which_history,cached_answer) in candidate.iter().enumerate() {
          cached_candidate[which_history] = cached_answer[new_history[which_history].len()..].to_vec();
        }
        cache.insert(key, cached_candidate);
      }

      candidate_list.push(candidate);
    }
  }
  candidate_list.dedup();

  // if candidate_list.len() > 1 {
  //   let labels = vec!["top left", "bottom right", "bottom left", "top right"];
  //   for c in &candidate_list {
  //     let mut candidate_distance = 0;
  //     for (i,robot) in c.iter().enumerate() {
  //       println!("{} {:?}", labels[i], robot);
  //       candidate_distance = candidate_distance + robot.iter().map(|(_c,d)| d).sum::<usize>();
  //     }
  //     println!("total distance {}", candidate_distance);
  //   }
  //   panic!("stop");
  // }

  let mut minimum = 9999;
  let mut smallest = vec![];

  while let Some(c) = candidate_list.pop() {
    let mut candidate_distance = 0;
    for c_history in &c {
      candidate_distance = candidate_distance + c_history.iter().map(|(_c,d)| d).sum::<usize>();
    }
    if candidate_distance < minimum {
      minimum = candidate_distance;
      smallest = c.clone();
    }
  }
  return smallest;
}
#[allow(dead_code)]
pub fn solve_part2(lines : Vec<String>) {

  let mut map = HashMap::new();
  let mut row = 0;
  let mut col = 0;
  let mut current_positions = vec![];
  let mut new_walls = vec![];

  for line in lines {
    for block in line.as_bytes() {
      if *block == b'@' {
        current_positions = vec![(row-1,col-1), (row+1,col+1), (row+1,col-1), (row-1,col+1)];
        new_walls = vec![(row+1,col),(row-1,col),(row,col-1),(row,col+1)];
        map.insert((row,col),Block{ position : (row,col), value : '#' as char, distance : 0});
      } else {
        map.insert((row,col),Block{ position : (row,col), value : *block as char, distance : 0});
      }
      col = col + 1;
    }
    col = 0;
    row = row + 1;
  }
  for p in new_walls {
    map.insert((p.0,p.1), Block{ position : (row,col), value : '#' as char, distance : 0});

  }

  print_map_2(&map, &current_positions);

  let retval = move_to_key_2(&map, &current_positions, &vec![], &mut HashMap::new());

  let labels = vec!["top left", "bottom right", "bottom left", "top right"];
  let mut distance = 0;
  for (robot,h) in retval.iter().enumerate() {
    distance = distance + h.iter().map(|(_c,d)| d).sum::<usize>();
    println!("Robot {}: {:?}", labels[robot], h);
  }

  println!("{}", distance);

}