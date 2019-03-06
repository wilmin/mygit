package com.appStore.tools;

import java.util.regex.Pattern;

public class InputValidJugdement {
	
	/*
	 * 
	 * 
	 *  // 反斜杠
		/t 间隔 ('/u0009')
		/n 换行 ('/u000A')
		/r 回车 ('/u000D')
		/d 数字 等价于[0-9]
		/D 非数字 等价于[^0-9]
		/s 空白符号 [/t/n/x0B/f/r]
		/S 非空白符号 [^/t/n/x0B/f/r]
		/w 单独字符 [a-zA-Z_0-9]
		/W 非单独字符 [^a-zA-Z_0-9]
		/f 换页符
		/e Escape
		/b 一个单词的边界
		/B 一个非单词的边界
		/G 前一个匹配的结束
		
		 
	    ^为限制开头
		^java     条件限制为以Java为开头字符
		$为限制结尾
		java$     条件限制为以java为结尾字符
		.条件限制除/n以外任意一个单独字符
		java..条件限制为java后除换行外任意两个字符
		加入特定限制条件「[]」
		[a-z]     条件限制在小写a to z范围中一个字符
		[A-Z]     条件限制在大写A to Z范围中一个字符
		[a-zA-Z] 条件限制在小写a to z或大写A to Z范围中一个字符
		[0-9]     条件限制在小写0 to 9范围中一个字符
		[0-9a-z] 条件限制在小写0 to 9或a to z范围中一个字符
		[0-9[a-z]] 条件限制在小写0 to 9或a to z范围中一个字符(交集)
		[^a-z]     条件限制在非小写a to z范围中一个字符
		
		[]中加入^后加再次限制条件「[^]」
		[^A-Z]     条件限制在非大写A to Z范围中一个字符
		[^a-zA-Z] 条件限制在非小写a to z或大写A to Z范围中一个字符
		[^0-9]     条件限制在非小写0 to 9范围中一个字符
		[^0-9a-z] 条件限制在非小写0 to 9或a to z范围中一个字符
		[^0-9[a-z]] 条件限制在非小写0 to 9或a to z范围中一个字符(交集)

		在限制条件为特定字符出现0次以上时，可以使用「*」
		J*     0个以上J
		.*     0个以上任意字符
		J.*D     J与D之间0个以上任意字符

		在限制条件为特定字符出现1次以上时，可以使用「+」
		J+     1个以上J
		.+     1个以上任意字符
		J.+D     J与D之间1个以上任意字符

		在限制条件为特定字符出现有0或1次以上时，可以使用「?」
		JA?     J或者JA出现

		限制为连续出现指定次数字符「{a}」
		J{2}     JJ
		J{3}     JJJ
		文字a个以上，并且「{a,}」
		J{3,}     JJJ,JJJJ,JJJJJ,???(3次以上J并存)
		文字个以上，b个以下「{a,b}」
		J{3,5}     JJJ或JJJJ或JJJJJ
		两者取一「|」
		J|A     J或A
		Java|Hello     Java或Hello
 
		「()」中规定一个组合类型
		
		在使用Pattern.compile函数时，可以加入控制正则表达式的匹配行为的参数：
		Pattern Pattern.compile(String regex, int flag)
		
		flag的取值范围如下：
		Pattern.CANON_EQ     当且仅当两个字符的"正规分解(canonical decomposition)"都完全相同的情况下，才认定匹配。比如用了这个标志之后，表达式"a/u030A"会匹配"?"。默认情况下，不考虑"规 范相等性(canonical equivalence)"。
		Pattern.CASE_INSENSITIVE(?i)     默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。这个标志能让表达式忽略大小写进行匹配。要想对Unicode字符进行大小不明感的匹 配，只要将UNICODE_CASE与这个标志合起来就行了。
		Pattern.COMMENTS(?x)     在这种模式下，匹配时会忽略(正则表达式里的)空格字符(译者注：不是指表达式里的"//s"，而是指表达式里的空格，tab，回车之类)。注释从#开始，一直到这行结束。可以通过嵌入式的标志来启用Unix行模式。
		Pattern.DOTALL(?s)     在这种模式下，表达式'.'可以匹配任意字符，包括表示一行的结束符。默认情况下，表达式'.'不匹配行的结束符。
		Pattern.MULTILINE
		(?m)     在这种模式下，'^'和'$'分别匹配一行的开始和结束。此外，'^'仍然匹配字符串的开始，'$'也匹配字符串的结束。默认情况下，这两个表达式仅仅匹配字符串的开始和结束。
		Pattern.UNICODE_CASE
		(?u)     在这个模式下，如果你还启用了CASE_INSENSITIVE标志，那么它会对Unicode字符进行大小写不明感的匹配。默认情况下，大小写不敏感的匹配只适用于US-ASCII字符集。
		Pattern.UNIX_LINES(?d)     在这个模式下，只有'/n'才被认作一行的中止，并且与'.'，'^'，以及'$'进行匹配。
	 * 
	 * */
	
	
	
	
	public static boolean isVersion(String in) {
		Pattern p = Pattern.compile("[0-9]*\\.[\\d]+");
		return p.matcher(in.trim()).matches();
	}

	public static boolean isMappingID(String in) {
		Pattern p = Pattern.compile("[1-9]{1}[0-9]*");
		return p.matcher(in.trim()).matches();
	}
}
