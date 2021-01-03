#[cfg(test)]
mod tests {
      use std::time::Instant;

      #[test]
    fn dec20_solve() {
      let lines: Vec<String> = include_str!("../inputs/dec22.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      super::solve_part1(&lines, 10007);
      println!("Complete in {:?}", start.elapsed());

      super::solve_part2(&lines,119315717514047);
    }
  }
  
use regex::Regex;
use std::time::Instant;

#[derive(Debug)]
pub enum Ops {
  NewStack,
  CutInto(i128),
  Transpose(usize),
}

lazy_static! {
  static ref NEW_STACK: Regex = Regex::new(r"^deal into new stack$").unwrap();
  static ref CUT_INTO: Regex = Regex::new(r"^cut (-*\d+)$").unwrap();
  static ref WITH_INCREMENT: Regex = Regex::new(r"^deal with increment (\d+)$").unwrap();
}

fn mod_inv(a: i128, module: i128) -> i128 {
  let mut mn = (module, a);
  let mut xy = (0, 1);
 
  while mn.1 != 0 {
    xy = (xy.1, xy.0 - (mn.0 / mn.1) * xy.1);
    mn = (mn.1, mn.0 % mn.1);
  }
 
  while xy.0 < 0 {
    xy.0 += module;
  }
  xy.0
}

fn transform(ops : &Vec<Ops>, stack_size : i128, pos : i128) -> i128 {
  let mut result = pos;

  for op in ops {
    result = single_transform(op, stack_size, result);
    // println!("{:?} -> {}", op, result);

  }
  return result;
}

fn reverse_transform(ops : &Vec<Ops>, stack_size : i128, result : i128) -> i128 {
  let mut input = result;
  for op in ops.iter().rev() {
    input = inverse_single_transform(&op, stack_size, input);
  }
  return input;
}

fn single_transform (op : &Ops, stack_size : i128, input : i128) -> i128 {
  match op {
    Ops::NewStack => stack_size - input - 1,
    Ops::CutInto(num) => (input as i128 - num) % stack_size,
    Ops::Transpose(num) => input*(*num as i128) % stack_size
  }
}

fn inverse_single_transform(op : &Ops, stack_size : i128, output : i128) -> i128 {
  match op {
    Ops::NewStack => stack_size - output - 1,
    Ops::CutInto(num) => (output + num) % stack_size,
    Ops::Transpose(num) => (output * mod_inv(*num as i128,stack_size)) % stack_size
  }
}

fn transform_expr(ops : &Vec<Ops>, stack_size : i128, pos : String) -> String {
  let mut result = pos;

  for op in ops {
    result = match op {
      Ops::NewStack => format!("({} - {} - 1)", stack_size, result),
      Ops::CutInto(num) => format!("({} - {} mod {})", result, num, stack_size),
      Ops::Transpose(num) => format!("({} * {} mod {})", result, num, stack_size)
    };

  }
  return result;
}

pub fn solve_part1(lines : &Vec<String>, stack_size : i128) {
  let mut ops = vec![];

  for line in lines {
    if let Some(args) = NEW_STACK.captures(line) {
      // println!("New stack");
      ops.push(Ops::NewStack);
    } else if let Some(args) = CUT_INTO.captures(line) {
      // println!("Cut into ({})", &args[1]);
      ops.push(Ops::CutInto(args[1].trim().parse::<i32>().unwrap() as i128));
    } else if let Some(args) = WITH_INCREMENT.captures(line) {
      // println!("With increment ({})", &args[1]);
      ops.push(Ops::Transpose(args[1].parse::<usize>().unwrap()));
    } else {
      panic!("unexpected line {}", line);
    }

  }
  
  // println!("{:?}",transform(&ops,stack_size,1));
  let start = 2019;
  let mut input = start;
  input = transform(&ops,stack_size,input);
  println!("result {}", input);
  println!("reverse part 1 {}", reverse_transform(&ops, stack_size, input));

}

pub fn solve_part2(lines : &Vec<String>, stack_size : i128) {
  let mut ops = vec![];

  for line in lines {
    if let Some(args) = NEW_STACK.captures(line) {
      // println!("New stack");
      ops.push(Ops::NewStack);
    } else if let Some(args) = CUT_INTO.captures(line) {
      // println!("Cut into ({})", &args[1]);
      ops.push(Ops::CutInto(args[1].trim().parse::<i32>().unwrap() as i128));
    } else if let Some(args) = WITH_INCREMENT.captures(line) {
      // println!("With increment ({})", &args[1]);
      ops.push(Ops::Transpose(args[1].parse::<usize>().unwrap()));
    } else {
      panic!("unexpected line {}", line);
    }

  }
  
  let result =  transform(&ops, stack_size,2020);
  println!("Result = {}",result);

  let calc : i128 = 2020 * 15359977617634; // mod inv of 
  let mod_inv_iteration = mod_inv(101741582076661, stack_size);
  println!("{} working with mod_inv compared to {}", mod_inv_iteration, 15359977617634i128);
  println!("result part 2 {}", reverse_transform(&ops, stack_size, calc));
  // 92665349929959 is too high
  // 4978573234469 is too low

  let mut result = 2020;
  for i in 0..15359977617634i128 {
    result = reverse_transform(&ops, stack_size, result);
    if i % 100000 == 0 { println!("{}", i as f64 / 15359977617634i128 as f64);}
  }
}