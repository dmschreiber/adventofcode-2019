//var initialState = [1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,19,10,23,2,10,23,27,1,27,6,31,1,13,31,35,1,13,35,39,1,39,10,43,2,43,13,47,1,47,9,51,2,51,13,55,1,5,55,59,2,59,9,63,1,13,63,67,2,13,67,71,1,71,5,75,2,75,13,79,1,79,6,83,1,83,5,87,2,87,6,91,1,5,91,95,1,95,13,99,2,99,6,103,1,5,103,107,1,107,9,111,2,6,111,115,1,5,115,119,1,119,2,123,1,6,123,0,99,2,14,0,0];
var initialState = [3,0,4,0,99];
var program;

const prompt = require('prompt');
prompt.start();

function performOpCode(opCode, arg1, arg2, arg3) {

	var offset = 0;

	if (opCode == 99) return -1;

	if (opCode == 1) {
		program[arg3] = program[arg1] + program[arg2];
		offset = 4;
	}
	
	if (opCode == 2) {
		program[arg3] = program[arg1] * program[arg2];
		offset = 4;
	}

	if (opCode == 3) {
		prompt.get(['input'], function(answer) {
			program[arg1] = answer;
			});

		offset = 2;
	}

	if (opCode == 4) {
		console.log("OUTPUT:" + program[arg1]);
		offset = 2;
	}

	return offset;
}

function runProgram() {
	var offsetIndex = 0;
	var offset = 0;

	while (offset >= 0) {
		offset = performOpCode(program[offsetIndex],program[offsetIndex+1],program[offsetIndex+2],program[offsetIndex+3]);
		offsetIndex += offset;
	}

	console.log("trying " + program[1] + ", " + program[2] + " result " + program[0]);
	return program[0];
}


var first = 12;
var second = 2;

program = initialState.slice(0);
program[1] = first;
program[2] = second;

runProgram();
console.log (first + ", " + second);
console.log (program[0]);
