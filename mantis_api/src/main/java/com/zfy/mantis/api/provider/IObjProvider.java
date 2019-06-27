package com.zfy.mantis.api.provider;

import com.zfy.mantis.annotation.LookupOpts;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public interface IObjProvider {

    Object getObject(LookupOpts opts);
}
