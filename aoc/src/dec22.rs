#[cfg(test)]
mod tests {
      use std::time::Instant;

      #[test]
    fn dec20_solve() {
      let lines: Vec<String> = include_str!("../inputs/dec22.txt").lines().map(|s| (&*s).to_string() ).collect();
      let start = Instant::now();
      super::solve_part1(&lines, 10007);
      println!("Complete in {:?}", start.elapsed());

      let lines: Vec<String> = include_str!("../inputs/dec22.txt").lines().map(|s| (&*s).to_string() ).collect();
      super::solve_part2(&lines,119315717514047, 101741582076661);
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

fn all_composite (ops: &Vec<Ops>, stack_size : i128) -> (i128, i128) {
  let mut input = (0,1);
  for op in ops {
    input = single_composite(op, stack_size, input.0, input.1);
  }
  return input;
}

fn single_composite (op : &Ops, stack_size : i128, add : i128, mult : i128) -> (i128,i128) {
  let ret = match op {
    Ops::NewStack => ((stack_size - add - 1).checked_rem_euclid(stack_size).unwrap(),-1*mult),
    Ops::CutInto(num) => ((add - *num as i128).checked_rem_euclid(stack_size).unwrap(),mult),
    Ops::Transpose(num) => (add*(*num as i128).checked_rem_euclid(stack_size).unwrap(),(mult*(*num as i128)).checked_rem_euclid(stack_size).unwrap())
  };
  return ret;
}

fn all_inverse_composite (ops: &Vec<Ops>, stack_size : i128) -> (i128, i128) {
  let mut input = (0,1);
  for op in ops.iter().rev() {
    input = single_inverse_composite(op, stack_size, input.0, input.1);
  }
  return input;
}

fn single_inverse_composite (op : &Ops, stack_size : i128, add : i128, mult : i128) -> (i128,i128) {
  let ret = match op {
    Ops::NewStack => ((stack_size - add - 1).checked_rem_euclid(stack_size).unwrap(),(-1*mult).checked_rem_euclid(stack_size).unwrap()),
    Ops::CutInto(num) => ((add + *num as i128).checked_rem_euclid(stack_size).unwrap(),mult.checked_rem_euclid(stack_size).unwrap()),
    Ops::Transpose(num) => ( (add*mod_inv(*num as i128,stack_size)).checked_rem_euclid(stack_size).unwrap(),(mult*mod_inv(*num as i128,stack_size)).checked_rem_euclid(stack_size).unwrap() )
  };
  return ret;  
}

fn inverse_single_transform(op : &Ops, stack_size : i128, output : i128) -> i128 {
  match op {
    Ops::NewStack => stack_size - output - 1,
    Ops::CutInto(num) => (output + num) % stack_size,
    Ops::Transpose(num) => (output * mod_inv(*num as i128,stack_size)) % stack_size
  }
}

// fn transform_expr(ops : &Vec<Ops>, stack_size : i128, pos : String) -> String {
//   let mut result = pos;

//   for op in ops {
//     result = match op {
//       Ops::NewStack => format!("({} - {} - 1)", stack_size, result),
//       Ops::CutInto(num) => format!("({} - {} mod {})", result, num, stack_size),
//       Ops::Transpose(num) => format!("({} * {} mod {})", result, num, stack_size)
//     };

//   }
//   return result;
// }

pub fn solve_part1(lines : &Vec<String>, stack_size : i128) {
  let mut ops = vec![];

  for line in lines {
    if let Some(_args) = NEW_STACK.captures(line) {
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
  let first_number = 2019;
  let mut input = first_number;
  let start = Instant::now();
  input = transform(&ops,stack_size,input);
  println!("transform in {:?}", start.elapsed());
  let eq_forward = all_composite(&ops, stack_size);
  let start = Instant::now();
  println!("result {} vs {}x+{}={}", input, eq_forward.1, eq_forward.0, (first_number * eq_forward.1 + eq_forward.0).rem_euclid(stack_size));
  println!("transform in {:?}", start.elapsed());
  let eq_back = all_inverse_composite(&ops, stack_size);
  assert!(first_number==(eq_back.1*(eq_forward.1*first_number + eq_forward.0).rem_euclid(stack_size) + eq_back.0).rem_euclid(stack_size));
  println!("reverse part 1 {} vs {}", reverse_transform(&ops, stack_size, input), (eq_back.1*input + eq_back.0).rem_euclid(stack_size) );

  let mut result = first_number;
  for _i in 0..10 {
    result = (( eq_forward.1*result).rem_euclid(stack_size) + eq_forward.0 ).rem_euclid(stack_size);
  }
  assert!(first_number==iterate_referse_shuffle(&ops, stack_size, 10, result));  
}
fn mod_pow(mut base: i128, mut exp: i128, modulus: i128) -> i128 {
  if modulus == 1 { return 0 }
  let mut result = 1;
  base = base % modulus;
  while exp > 0 {
      if exp % 2 == 1 {
          result = result * base % modulus;
      }
      exp = exp >> 1;
      base = base * base % modulus
  }
  result
}

fn solve_the_iteration(add : i128, mult : i128, stack_size : i128, iterations : i128, input : i128) -> i128 {

  let part1 = (mod_pow(mult,iterations,stack_size)*input).rem_euclid(stack_size);
  let part2 = add.rem_euclid(stack_size);
  let part3 = ((mod_pow(mult,iterations,stack_size)-1)*mod_inv((mult - 1).rem_euclid(stack_size),stack_size)).rem_euclid(stack_size);

  let test1 = (mult-1).rem_euclid(stack_size);
  let test2 = mod_inv( (mult - 1).rem_euclid(stack_size),stack_size);

  assert!((test1*test2).rem_euclid(stack_size)==1);

  let mut result = part1 + (part2*part3).rem_euclid(stack_size);
  result = result.rem_euclid(stack_size);
  result
}

fn iterate_referse_shuffle(ops : &Vec<Ops>, stack_size : i128, iterations : i128, output : i128) -> i128 {
  let (add,mult) = all_inverse_composite(ops, stack_size);

  return solve_the_iteration(add, mult, stack_size, iterations, output);
}

#[allow(dead_code)]
fn iterate_shuffle(ops : &Vec<Ops>, stack_size : i128, iterations : i128, input : i128) -> i128 {
  let (add,mult) = all_composite(ops, stack_size);
  return solve_the_iteration(add, mult, stack_size, iterations, input);
}

pub fn solve_part2(lines : &Vec<String>, stack_size : i128, iterations : i128) {
  let mut ops = vec![];

  for line in lines {
    if let Some(_args) = NEW_STACK.captures(line) {
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


  let mut result_calc;

  let new_eq = all_inverse_composite(&ops, stack_size);
  result_calc = 2020;
  for i in 0..10 {  
    // result = reverse_transform(&ops, stack_size, result);
    result_calc = ((new_eq.1 * result_calc).rem_euclid(stack_size) + new_eq.0).rem_euclid(stack_size);
    assert!(result_calc==iterate_referse_shuffle(&ops, stack_size, i+1, 2020));
  }
  assert!(2020==iterate_shuffle(&ops, stack_size, 10, result_calc));
  println!("Over {} iterations result is {}", iterations, iterate_referse_shuffle(&ops, stack_size, iterations, 2020).rem_euclid(stack_size));
}