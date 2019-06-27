package com.zfy.mantis.compiler;

import com.zfy.mantis.annotation.Lookup;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public class MantisConst {

    public static final String CLASS_SUFFIX          = Lookup.SEP + Lookup.SUFFIX;
    public static final String SYRINGE_INTERFACE     = "com.zfy.mantis.api.ISyringe";
    public static final String INJECT_ARGS_INTERFACE = "com.zfy.mantis.api.IArgsInject";
    public static final String METHOD_NAME           = "inject";
    public static final String METHOD_PARAM_TARGET   = "target";
    public static final String METHOD_PARAM_GROUP    = "group";
    public static final String CLASS_LOOKUP_OPTS     = "com.zfy.mantis.annotation.LookupOpts";

    public static final String PARCELABLE = "android.os.Parcelable";

}
