package org.openlca.olcatdb.conversion;

/**
 * Utilities for generating pseudo-UUIDs from strings.
 * 
 * @author Michael Srocka
 * 
 */
public class KeyGen {

	/**
	 * Get a hex number for the string with the given length, e.g.
	 * <code>getKey("a-test",6)</code> returns <code>c98582</code>.
	 */
	public static String getKey(String value, int length) {
		String key = getKey(value);
		if (key.length() > length) {
			key = key.substring(key.length() - length);
		} else if (key.length() < length) {
			int diff = length - key.length();
			while (diff > 0) {
				key = "0".concat(key);
				diff--;
			}
		}
		return key;
	}

	/**
	 * Get a hex-number for the given string, e.g. <code>getKey("a-test")</code>
	 * returns <code>57c98582</code>.
	 */
	public static String getKey(String value) {
		int k = Math.abs(value.hashCode());
		StringBuffer keyBuffer = new StringBuffer();
		do {
			int mod = k % 16;
			switch (mod) {
			case 10:
				keyBuffer.insert(0, 'a');
				break;
			case 11:
				keyBuffer.insert(0, 'b');
				break;
			case 12:
				keyBuffer.insert(0, 'c');
				break;
			case 13:
				keyBuffer.insert(0, 'd');
				break;
			case 14:
				keyBuffer.insert(0, 'e');
				break;
			case 15:
				keyBuffer.insert(0, 'f');
				break;
			default:
				keyBuffer.insert(0, Integer.toString(mod));
				break;
			}
			k = k / 16;
		} while (k != 0);
		return keyBuffer.toString();
	}

	/**
	 * Get a pseudo-UUID for the given string arguments, e.g. the call
	 * <code>getPseodoUUID("this", "is", "a", "test")</code> returns
	 * <code>0035dde2-be0c-91d3-edbf-00000134cd60</code> where the call
	 * <code>getPseodoUUID()</code> returns
	 * <code>00000000-0000-0000-0000-000000000000</code>.
	 */
	public static String getPseodoUUID(String... args) {
		StringBuilder feed = new StringBuilder();
		StringBuilder key = new StringBuilder();

		if (args.length > 0 && args[0] != null) {
			feed.append(args[0]);
		}
		feed.reverse();
		key.append(getKey(feed.toString(), 8)).append("-");

		if (args.length > 1 && args[1] != null) {
			feed.append(args[1]);
		} else {
			feed.append(feed.toString());
		}
		feed.reverse();
		key.append(getKey(feed.toString(), 4)).append("-");

		if (args.length > 2 && args[2] != null) {
			feed.append(args[2]);
		} else {
			feed.append(feed.toString());
		}
		feed.reverse();
		key.append(getKey(feed.toString(), 4)).append("-");

		if (args.length > 3 && args[3] != null) {
			feed.append(args[3]);
		} else {
			feed.append(feed.toString());
		}
		feed.reverse();
		key.append(getKey(feed.toString(), 4)).append("-");

		if (args.length > 4 && args[4] != null) {
			feed.append(args[4]);
		} else {
			feed.append(feed.toString());
		}
		feed.reverse();
		key.append(getKey(feed.toString(), 12));

		return key.toString();
	}

}
