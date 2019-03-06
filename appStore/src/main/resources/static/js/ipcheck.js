function validateIPaddress( ip ) {  
    if( /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test( ip ) ) {  
        return true;
    }  
    return false;
}

function validateIPAddressBlock(begin, end) {
	if(validateIPaddress(begin) && validateIPaddress(end)) {
		var beginStrs= new Array(); //定义一数组 
		var endStrs= new Array(); //定义一数组 
		beginStrs = begin.split("."); //字符分割 
		endStrs = end.split("."); //字符分割 
		var headIsLegal = true;
		for (i = 0; i < beginStrs.length - 1; i++) 
		{ 
			if(beginStrs[i] != endStrs[i]) headIsLegal = false;
		}
		if(headIsLegal) {
			if(beginStrs[3] <= endStrs[3])
				return true;
		}
		return false;
	}
	return false;
}