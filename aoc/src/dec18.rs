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
// use std::time::Instant;

#[derive(Debug,Clone,Copy)]
pub struct Block {
  position : (usize,usize),
  value : char,
  distance : usize,
}

impl Block {
  #[allow(dead_code)]
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
  #[allow(dead_code)]
  fn is_key(&self) -> bool {
    return  self.value >= 'a' && self.value <= 'z';

  }
  #[allow(dead_code)]
  fn is_wall(&self) -> bool {
    return self.value == '#' || (self.value >= 'A' && self.value <= 'Z');
  }
  #[allow(dead_code)]
  fn distance(&self, position : (usize,usize)) -> usize {
    return (position.0 as i32- self.position.0 as i32).abs() as usize + (position.1 as i32- self.position.1 as i32).abs() as usize;
  }
}
#[allow(dead_code)]
fn path(map : &HashMap<(usize,usize),Block>, point_a : (usize,usize), point_b : (usize,usize), history : Vec<(usize,usize)>) -> Option<usize> {
  let mut min_distance = 9999;

  if point_a == point_b { return Some(history.len()); }
  if let Some(b) = map.get(&point_a) {
    for n in b.get_neighbors() {
      if !history.contains(&n) {
        if let Some(n_o) = map.get(&n) {
          if !n_o.is_wall() && (!n_o.is_key() || n == point_b) {
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
fn move_to_key(map : HashMap<(usize,usize),Block>, current_position : (usize, usize), history : Vec<Block>, cache : &mut HashMap<String,Vec<(char,usize)>>) -> Vec<(char,usize)> {
  if map.values().filter(|b| b.is_key()).count() == 0 {
    let retval = history.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
    return retval;
  }

  let mut candidate_list : Vec<Vec<(char,usize)>> = vec![];
  let blocks = map.values()
                .filter(|b| b.is_key())
                .map(|b| (b,path(&map, current_position, b.position, vec![])))
                .filter(|(_b,d)| *d != None)
                .map(|(b,d)| Block{ position : b.position, distance : d.unwrap(), value : b.value})
                .collect::<Vec<Block>>();

  // if history.len() == 3  {
    for _i in 0..history.len() { print!(">"); }
    println!("History {:?} Exploring {:?} options", history.iter().map(|b| b.value).collect::<Vec<char>>(), blocks.iter().map(|b| b.value).collect::<Vec<char>>());
  // }

  for (_i,block) in blocks.iter().enumerate() {
    let new_position = block.position;
    let block_distance = block.distance;

    // if history.iter().map(|b| b.distance).sum::<usize>() + d > v.iter().map(|(_c,d)| d).sum::<usize>() {
    //   panic!("history distance {}, about to move {}, historical shortest distance {}", history.iter().map(|b| b.distance).sum::<usize>(), d, v.iter().map(|(_c,d)| d).sum::<usize>());
    // }
    let mut new_history = history.clone();
    let mut block_copy = block.clone().to_owned();
    block_copy.distance = block_distance;
    new_history.push(block_copy);

    // println!("{:?} to {} is {:?} ({} steps, historical distance {:?}) shortest {}", current_position, block.value, d, history.len(), history.iter().map(|b| b.distance).sum::<usize>(), shortest);
    let mut new_map = map.clone();
    let door_vec = new_map.values().filter(|b| b.value == ('A' as u8 + block.value as u8 -b'a') as char).map(|b| b.position ).collect::<Vec<(usize,usize)>>();
    if door_vec.len() > 0 {
      new_map.get_mut(&door_vec[0]).unwrap().value = '.' // remove door
    } 
    new_map.get_mut(&new_position).unwrap().value = '.'; // remove key

    let v_candidate : Vec<(char,usize)>;
    // let start = Instant::now();
    let key = format!("{:?} {:?}", new_position, new_map.values().filter(|b| b.is_key()).map(|b| b.value).collect::<Vec<char>>());

    if let Some(temp) = cache.get(&key) {
      let mut front = history.iter().map(|b| (b.value,b.distance) ).collect::<Vec<(char,usize)>>();
      let mut middle = vec![(block.value, block_distance)];
      let mut back = temp.clone()[front.len()+1..].to_vec();
      front.append(&mut middle);
      front.append(&mut back);
      v_candidate = front;
      // println!("found in cache {}, created {:?}", &key, v_candidate);
    } else {
      v_candidate= move_to_key(new_map.clone(), new_position, new_history.clone(), cache);
      // println!("storing in cache {} - {:?}", &key, &v_candidate);
      cache.insert(key,v_candidate.clone());
    }
    // println!("appending {:?} with {:?}", candidate_list, v_candidate);
    candidate_list.push(v_candidate.clone());
    candidate_list.dedup();

  }
  let mut minimum = candidate_list[0].iter().map(|(_c,distance)| distance).sum::<usize>();
  let mut smallest = candidate_list[0].clone();
  // println!("I have {} results", candidate_list.len());
  if candidate_list.len() > 1 {
    for a in &candidate_list {      
      let my_size = a.iter().map(|(_c,distance)| distance).sum::<usize>();
      if  my_size < minimum {
        minimum = my_size ;
        smallest = a.clone();
      }
      // println!("{:?}: {}", a, a.iter().map(|(c,d)| d).sum::<usize>());
    }
  }
  return smallest;
 
}

#[allow(dead_code)]
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

  print_map(&map, current_position);

  let retval = move_to_key(map, current_position, vec![], &mut HashMap::new());
  println!("{:?}", retval);
  println!("{}", retval.iter().map(|(_a,b)| b).sum::<usize>());
}