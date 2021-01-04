#[cfg(test)]
mod tests {
      use std::time::Instant;

    #[test]
    fn dec24_solve() {
      let lines: Vec<String> = include_str!("../inputs/dec24.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      println!("part 1 result {}",super::solve_part1(&lines));
      println!("Complete in {:?}", start.elapsed());

      println!("part 2 result {}",super::solve_part2(&lines,200));

    }
    use std::collections::HashMap;
    
    #[test]
    fn dec24_tests() {
      let pattern = 
      vec![".....",
          ".....",
          ".....",
          "#....",
          ".#..."].iter().map(|s| (*s).to_string()).collect();

      let pattern2 = 
      vec!["....#",
          "#..#.",
          "#..##",
          "..#..",
          "#...."].iter().map(|s| (*s).to_string()).collect();;

      let map = super::read_map(&pattern);
      assert!(2129920==super::calc_bio(&map));

      assert!(2129920==super::solve_part1(&pattern2));

      assert!(2==super::count_bugs(&map));
      let mut fake_map = HashMap::new();
      fake_map.insert(0,map.clone());
      fake_map.insert(-1,map.clone());
      fake_map.insert(1,map.clone());

      assert!(6==super::count_all_bugs(&fake_map));
      assert!(super::get_neighbors(0,0,0)==vec![(1,0,0),(0,1,0) ,(1,2,-1),(2,1,-1)]);
      assert!(super::get_neighbors(2,1,0)==vec![(1,1,0), (3,1,0),(2,0,0),(0,0,1),(1,0,1),(2,0,1),(3,0,1),(4,0,1)]);

      assert!(99==super::solve_part2(&pattern2,10));
    }
  }
  
  use std::collections::HashMap;
  use std::convert::TryInto;

  pub fn get_spot(map : &HashMap<(isize,isize),char>, row : isize, col : isize) -> char {
    if let Some(b) = map.get( &( row, col ) ) {
      if *b == '#' {
        return *b;
      } else {
      return '.';
      }
    }
    return '.';
  }

  pub fn calc_bio(map : &HashMap<(isize,isize), char>) -> u32{
    let mut total = 0;
    for i in 0..25 {
      if '#' == get_spot(map, (i/5).try_into().unwrap(), (i%5).try_into().unwrap()) {
        total = total + 2u32.pow(i);
      }
    }
    return total;
  }
  pub fn read_map(lines : &Vec<String>) -> HashMap<(isize,isize),char> {

    let mut map = HashMap::new();

    for (row,line) in lines.iter().enumerate() {
      for (col,value) in line.as_bytes().iter().enumerate() {
        map.insert((row as isize,col as isize),line.as_bytes()[col] as char);
      }
    }
    return map;
  }

  pub fn one_turn(map : &HashMap<(isize,isize),char>) -> HashMap<(isize,isize),char> {
    let mut new_map = HashMap::new();
    for i in 0..25 {
      let row = i/5;
      let col = i % 5;
      let mut neighbor_count = 0;
      for n in vec![(0,1), (1,0), (0,-1), (-1,0)] {
        if get_spot(map, row+n.0,col+n.1) == '#' {
          neighbor_count += 1;
        }
      }
      if get_spot(map, row,col) == '#' && neighbor_count != 1 {
        new_map.insert((row,col),'.');
      } else if neighbor_count >= 1 && neighbor_count <=2 {
        new_map.insert((row,col),'#');
      } else {
        new_map.insert((row,col),get_spot(map,row,col));
      }
    }
    return new_map;
  }

  fn print_map(map : &HashMap<(isize,isize),char>) {
    for i in 0..25 {
      let row = i/5;
      let col = i % 5;
      if col == 0 {println!();}
      print!("{}", get_spot(map, row, col));
    }
  }
  pub fn solve_part1(lines : &Vec<String>) -> u32 {
    let mut retval = 0;
    let mut history = vec![];

    let mut map = read_map(lines);
    retval = calc_bio(&map);

    while !history.contains(&retval) {  
      // println!("TURN");
      // print_map(&map);    
      history.push(retval);
      map = one_turn(&map);
      retval = calc_bio(&map);
    }
    println!("FINAL");
    print_map(&map);    
  return retval;
  }

  fn count_bugs(map : &HashMap<(isize,isize),char>) -> u32 {
    return map.values().filter(|c| **c == '#').count().try_into().unwrap();
  }

  fn count_all_bugs(map : &HashMap<isize,HashMap<(isize,isize),char>>) -> u32 {
    return map.values().map(|m| m.values().filter(|c| **c == '#').count() as u32).sum();
  }


  pub fn get_part2_spot(map : &HashMap<isize,HashMap<(isize,isize),char>>, row : isize, col : isize, level : isize) -> char {
    if let Some(m) = map.get(&level) {
      if let Some(b) = m.get( &( row, col ) ) {
        if *b == '#' {
          return *b;
        } else {
        return '.';
        }
      }
    }
    return '.';
  }
  
  fn basic_neighbors(row : isize, col : isize, level : isize) -> Vec<(isize,isize,isize)> {
    let mut n = vec![];

    for candidate in vec![(-1,0),(1,0),(0,-1),(0,1)] {
      let n_row = candidate.0 + row;
      let n_col = candidate.1 + col;
      if n_row == 2 && n_col == 2 { 
        // don't add
      }
      else if n_row >= 0 && n_row < 5 && n_col >= 0 && n_col < 5 {
        n.push ((n_row, n_col, level));
      }
    }
    return n;
  }

  fn inner_neighbors(row : isize, col : isize, level : isize) -> Vec<(isize,isize,isize)> {
    if row == 2 && col == 1 { (0..5).map(|r| (r,0,level+1)).collect() }
    else if row == 2 && col == 3 {  (0..5).map(|r| (r,4,level+1)).collect() }
    else if row == 1 && col == 2 {  (0..5).map(|c| (0,c,level+1)).collect() }
    else if row == 3 && col == 2 {  (0..5).map(|c| (4,c,level+1)).collect() }    
    else { vec![] }
  }

  fn outer_neighbors(row : isize, col : isize, level : isize) -> Vec<(isize,isize,isize)> {
    if row == 0 && col == 0 {vec![(1,2,level-1), (2,1,level-1)] }
    else if row == 0 && col == 4 { vec![(1,2,level-1), (2,3,level-1)] }
    else if row == 4 && col == 0 { vec![(3,2,level-1), (2,1,level-1)]}
    else if row == 4 && col == 4 { vec![(3,2,level-1), (2,3,level-1)]}
    else if row == 0 { vec![(1,2,level-1) ] }
    else if row == 4 { vec![(3,2,level-1) ] }
    else if col == 0 { vec![(2,1,level-1) ] }
    else if col == 4 { vec![(2,3,level-1) ] }
    else { vec![] }
  }

  fn get_neighbors(row : isize, col : isize, level : isize) -> Vec<(isize,isize,isize)> {
    let mut n = basic_neighbors(row, col, level);
    let mut inner_neighbors = inner_neighbors(row, col, level);
    let mut outer_neighbors = outer_neighbors(row,col,level);
    n.append(&mut inner_neighbors);
    n.append(&mut outer_neighbors);

    return n;

  }

  // level -1 contains level 0, level 0 contains level 1
  fn create_level(map : &HashMap<isize,HashMap<(isize,isize),char>>, level : isize) -> HashMap<(isize,isize),char> {
    let mut new_map = HashMap::new();
    for i in 0..25 {
      let row = i/5;
      let col = i % 5;
      if !(row == 2 && col == 2) {
        let mut neighbor_count = 0;
        for n in get_neighbors(row,col,level) {
          if get_part2_spot(map, n.0,n.1,n.2) == '#' {
            neighbor_count += 1;
          }
        }

        if get_part2_spot(&map, row,col,level) == '#' && neighbor_count != 1 {
          new_map.insert((row,col),'.');
        } else if neighbor_count >= 1 && neighbor_count <=2 {
          new_map.insert((row,col),'#');
        } else {
          new_map.insert((row,col),get_part2_spot(&map,row,col,level));
        }
      }
    }
    return new_map;
  }

  fn one_part2_turn(map : &HashMap<isize,HashMap<(isize,isize),char>>) -> HashMap<isize,HashMap<(isize,isize),char>>
  {
    let mut new_map = HashMap::new();
    let min_level = *map.keys().min().unwrap();
    let max_level = *map.keys().max().unwrap();

    for level in min_level-1..=max_level+1 {
      let sub_map = create_level(map, level);

      if count_bugs(&sub_map) > 0 {
        new_map.insert(level,sub_map);
      }
    }
    return new_map;
  }

  pub fn solve_part2(lines : &Vec<String>, minutes : u32) -> u32 {
    let mut retval = 0;
    let mut map = HashMap::new();

    map.insert(0,read_map(lines));

    for i in 0..minutes {
      map = one_part2_turn(&map);
      println!("After {} minutes", i+1);
    }
    return count_all_bugs(&map);

  }