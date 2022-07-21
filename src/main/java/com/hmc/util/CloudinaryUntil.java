package com.hmc.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUntil {

	private static final String cloudName = "dzez3wufo";
	private static final String apiKey = "818974938696992";
	private static final String apiSecret = "GLenE2XXbJVDpr5SzDLuTXlvQ3A";
	private static final boolean secure = true;
	
	
	public static Cloudinary cloudinary() {
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", secure
        ));
        return c;
    }
	
}
