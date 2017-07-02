/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Mar 1, 2012
 */

package com.greenorbs.tagassist.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.greenorbs.tagassist.BaggageInfo;

public class BaggageUtils {
	
	private static final Pattern _baggageNumberPattern = Pattern.compile("[0-9]{10}");
	
	public static final boolean isValid(String baggageNumber) {
		Matcher m = _baggageNumberPattern.matcher(baggageNumber);
		return m.matches();
	}
	
	public static final int STATUS_CATEGORY_UNKNOWN  = 0;
	public static final int STATUS_CATEGORY_SORTED 	 = 1;
	public static final int STATUS_CATEGORY_UNSORTED = 2;
	public static final int STATUS_CATEGORY_MISSING  = 3;
	public static final int STATUS_CATEGORY_REMOVED  = 4;
	
	public static int getStatusCategory(int status) {
		switch (status) {
		case BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_RIGHT:
		case BaggageInfo.STATUS_READ_BY_MOBILEREADER_RIGHT:
		case BaggageInfo.STATUS_BOARDED:
		{
			return STATUS_CATEGORY_SORTED;
		}
		case BaggageInfo.STATUS_ARRIVED:
		case BaggageInfo.STATUS_IN_POOL:
		case BaggageInfo.STATUS_READ_BY_WRISTBAND:
		case BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_WRONG:
		case BaggageInfo.STATUS_READ_BY_MOBILEREADER_WRONG:
		{
			return STATUS_CATEGORY_UNSORTED;
		}
		case BaggageInfo.STATUS_MISSING:
		{
			return STATUS_CATEGORY_MISSING;
		}
		case BaggageInfo.STATUS_REMOVED:
		{
			return STATUS_CATEGORY_REMOVED;
		}
		case BaggageInfo.STATUS_UNKNOWN:
		default:
			return STATUS_CATEGORY_UNKNOWN;
		}
	}
	
	public static boolean isStatusCategoryChanged(int oldStatus, int newStatus) {
		return getStatusCategory(oldStatus) != getStatusCategory(newStatus);
	}

	public static boolean isSorted(int status) {
		return getStatusCategory(status) == STATUS_CATEGORY_SORTED;
	}
	
	public static boolean isUnsorted(int status) {
		return getStatusCategory(status) == STATUS_CATEGORY_UNSORTED;
	}
	
	public static boolean isMissing(int status) {
		return getStatusCategory(status) == STATUS_CATEGORY_MISSING;
	}
	
	public static boolean isRemoved(int status) {
		return getStatusCategory(status) == STATUS_CATEGORY_REMOVED;
	}
	
	public static boolean isWronglySorted(int status) {
		return (status == BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_WRONG || 
				status == BaggageInfo.STATUS_READ_BY_MOBILEREADER_WRONG);
	}
	
	public static boolean isSorted(BaggageInfo baggageInfo) {
		return isSorted(baggageInfo.getStatus());
	}
	
	public static boolean isUnsorted(BaggageInfo baggageInfo) {
		return isUnsorted(baggageInfo.getStatus());
	}
	
	public static boolean isMissing(BaggageInfo baggageInfo) {
		return isMissing(baggageInfo.getStatus());
	}
	
	public static boolean isRemoved(BaggageInfo baggageInfo) {
		return isRemoved(baggageInfo.getStatus());
	}
	
	public static boolean isWronglySorted(BaggageInfo baggageInfo) {
		return isWronglySorted(baggageInfo.getStatus());
	}
	
}
