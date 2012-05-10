package com.egroen.bukkit.gsurvivalextras;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Tools - Tiny toolbox
 * 
 * Some tools for easy working.
 * @author egroen
 *
 */
public class Tools {

	/**
	 * Create an hash for vectors
	 * Returns a string like Vx{X-COORD}y{Y-COORD}z{Z-COORD}
	 * @param vector
	 * @return
	 */
	public static String vector2Hash(org.bukkit.util.Vector vector) {
		return new StringBuilder().append("Vx").append(vector.getBlockX()).append("y").append(vector.getBlockY()).append("z").append(vector.getBlockZ()).toString();
	}
	
	/**
	 * Creates an bukkit-type vector from a hash
	 * Reads a string like Vx{X-COORD}y{Y-COORD}z{Z-COORD}
	 * @param hash
	 * @return
	 */
	public static org.bukkit.util.Vector hash2Vector(String hash) {
		String[] elements = preg_match("Vx(-?[0-9]+)y(-?[0-9]+)z(-?[0-9]+)", hash, true);
		if (elements.length != 4) return null;
		return new org.bukkit.util.Vector(
				Integer.parseInt(elements[1]),
				Integer.parseInt(elements[2]),
				Integer.parseInt(elements[3])
			);
	}
	
	/**
	 * Glue array together
	 * @param strings
	 * @return
	 */
	public static String glue(String[] strings) { return glue(strings, 0, strings.length-1); }
	/**
	 * Glue array together starting at
	 * @param strings
	 * @param start
	 * @return
	 */
	public static String glue(String[] strings, int start) { return glue(strings, start, strings.length-1); }
	/**
	 * Glue array together starting at .. till ..
	 * @param strings
	 * @param start
	 * @param end
	 * @return
	 */
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
     * @param safe Safe the pattern?
     * @return Groups in array
     * @throws PatternSyntaxException 
     */
    public static String[] preg_match(String regex, String subject) throws PatternSyntaxException {
	    return preg_match(regex, subject, false);
	}
    
    /**
     * Runs a perl-regular expression
     * and returns groups in array
     * @param regex The regular Expression
     * @param subject The string to read
     * @param safe Safe the pattern?
     * @return Groups in array
     * @throws PatternSyntaxException 
     */
    public static String[] preg_match(String regex, String subject, boolean safe) throws PatternSyntaxException {
        Pattern pattern;
        if (_patterns.containsKey(regex)) {
            pattern = _patterns.get(regex);
        } else {
            pattern = Pattern.compile(regex);
            if (safe) _patterns.put(regex, pattern);
        }
        
        Matcher matcher = pattern.matcher(subject);
        boolean matchFound = matcher.find();
        if (!matchFound) return null;
        
        String[] lines = new String[matcher.groupCount()+1];
        for (int i=0; i<=matcher.groupCount(); i++)
            lines[i] = matcher.group(i);
        return lines;
    }
    /**
     * Hashmap to keep patterns for faster working.
     */
    private static HashMap<String, Pattern> _patterns = new HashMap<String, Pattern>();
}
