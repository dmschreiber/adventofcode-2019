

function meets(number) {
//	console.log("calling meets on " + number);
	var lastNumber = 0;
	var index = 0;
	var isRepeated = false;
	var isOnlyTwo = false;

//	console.log(number.toString().length);

//	increasing digits
	for (index=0;index<number.toString().length;index++) {
		var digit = number.toString().substring(index,index+1);	
//		console.log(digit);
		if (digit < lastNumber) {
//			console.log("not increasing");
			return false;
		}

		lastNumber = digit;
	}
	
	for (index = 0; index<10; index++) {
		good = index.toString() + index.toString();
		bad = index.toString() + index.toString() + index.toString();

		if ( (number.toString().indexOf(good) >= 0) && (number.toString().indexOf(bad) < 0) ) {
		//	console.log(good + " not " + bad + " (" + number + ")");
			isOnlyTwo = true;
		}

	}
	return isOnlyTwo;
}

var start = process.argv[2];
var end = process.argv[3];
//console.log("trying " + start + " to " + end);

for (i=start; i<=end; i++) {
	if (meets(i) > 0) {
		console.log(i);
	}
}
