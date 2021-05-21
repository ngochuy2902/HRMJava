package com.nals.hrm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter

public class ErrorCodes {
    public static Map<String, String> code;
    static {
        code = new HashMap<>();
        code.put("Pattern", "VAL_001");
        code.put("NotBlank", "VAL_002");
        code.put("Size", "VAL_003");
        code.put("Date", "VAL_004");
        code.put("Conflict", "VAL_005");
        code.put("Email", "VAL_006");
        code.put("Regex", "VAL_007");
        code.put("Max", "VAL_008");
        code.put("Min", "VAL_009");
        code.put("NotNull", "VAL_011");
        code.put("NotFound", "VAL_012");
    }
}
