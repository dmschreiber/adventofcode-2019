var initialState = [1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,19,10,23,2,10,23,27,1,27,6,31,1,13,31,35,1,13,35,39,1,39,10,43,2,43,13,47,1,47,9,51,2,51,13,55,1,5,55,59,2,59,9,63,1,13,63,67,2,13,67,71,1,71,5,75,2,75,13,79,1,79,6,83,1,83,5,87,2,87,6,91,1,5,91,95,1,95,13,99,2,99,6,103,1,5,103,107,1,107,9,111,2,6,111,115,1,5,115,119,1,119,2,123,1,6,123,0,99,2,14,0,0];
var program;

function performOpCode(opCode, arg1, arg2, arg3) {

	if (opCode == 99) return false;

	if (opCode == 1) {
		program[arg3] = program[arg1] + program[arg2];
	}
	
	if (opCode == 2) {
		program[arg3] = program[arg1] * program[arg2];
	}

	return true;
}

function runProgram() {
	var offset = 0;

	while (performOpCode(program[offset],program[offset+1],program[offset+2],program[offset+3])) {
		offset += 4;
	}

	console.log("trying " + program[1] + ", " + program[2] + " result " + program[0]);
	return program[0];
}


var first = 0;
var second = 0;

program = initialState.slice(0);
program[1] = first;
program[2] = second;

while ((runProgram() != 19690720) && (first <= 99)) {
	program = initialState.slice(0);
	if (second >= 99) {
		second = 0;
		++first;

	} else {
		++second;
	}
	program[1] = first;
	program[2] = second;


}
console.log (first + ", " + second);
