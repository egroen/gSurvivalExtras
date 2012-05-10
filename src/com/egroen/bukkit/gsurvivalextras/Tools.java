package com.egroen.bukkit.gsurvivalextras;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Tools {

	
	public static String vector2Hash(org.bukkit.util.Vector vector) {
		return new StringBuilder().append("Vx").append(vector.getBlockX()).append("y").append(vector.getBlockY()).append("z").append(vector.getBlockZ()).toString();
	}
	
	public static org.bukkit.util.Vector hash2Vector(String hash) {
		String[] elements = preg_match("Vx(-?[0-9]+)y(-?[0-9]+)z(-?[0-9]+)", hash);
		if (elements.length != 4) return null;
		return new org.bukkit.util.Vector(
				Integer.parseInt(elements[1]),
				Integer.parseInt(elements[2]),
				Integer.parseInt(elements[3])
			);
	}
	
	public static String glue(String[] strings) { return glue(strings, 0, strings.length-1); }
	
	public static String glue(String[] strings, int start) { return glue(strings, start, strings.length-1); }
	
	public static String glue(String[] strings, int start, int end) {
		if (start > end) return "";
		if (start < 0) start = 0;
		if (start > strings.length-1) return "";
		if (end > strings.length) end = strings.length-1;

		StringBuilder sb = new StringBuilder(strings[start]);
		for (int i = start+1; i <= end; i++) {
			sb.append(" ").append(strings[i]);
		}
		return sb.toString();
	}
	
	
    /**
     * Runs a perl-regular expression
     * and returns groups in array
     * @param regex The regular Expression
     * @param subject The string to read
     * @return Groups in array
     * @throws PatternSyntaxException 
     */
    public static String[] preg_match(String regex, String subject) throws PatternSyntaxException {
        Pattern pattern;
        if (!_patterns.containsKey(regex)) {
        	_patterns.put(regex, Pattern.compile(regex));
        }
        pattern = _patterns.get(regex);
        
        Matcher matcher = pattern.matcher(subject);
        boolean matchFound = matcher.find();
        if (!matchFound) return null;
        
        String[] lines = new String[matcher.groupCount()+1];
        for (int i=0; i<=matcher.groupCount(); i++)
            lines[i] = matcher.group(i);
        return lines;
    }
    private static HashMap<String, Pattern> _patterns = new HashMap<String, Pattern>();
}
