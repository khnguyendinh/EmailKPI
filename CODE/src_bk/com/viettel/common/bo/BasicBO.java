/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.common.bo;

import java.io.Serializable;

/**
 * Doi tuong BO co ban.
 * @author NVH
 * @version 1.0
 * @since 1.0
 */
public abstract class BasicBO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public BasicBO() {
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
}
