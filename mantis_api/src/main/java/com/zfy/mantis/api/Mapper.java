package com.zfy.mantis.api;

import com.zfy.mantis.annotation.LookupOpts;

/**
 * CreateAt : 2019-11-08
 * Describe :
 *
 * @author chendong
 */
public interface Mapper<T> {

    T map(LookupOpts mapOpts);

    default int priority() {
        return Mantis.DEFAULT_PRIORITY + 1;
    }
}
