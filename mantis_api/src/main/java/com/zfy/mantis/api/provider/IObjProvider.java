package com.zfy.mantis.api.provider;

import com.zfy.mantis.annotation.LookupOpts;

/**
 * CreateAt : 2019/1/29
 * Describe : 取对象，一般用来生成对象
 *
 * @author chendong
 */
public interface IObjProvider {

    Object getObject(LookupOpts opts);
}
