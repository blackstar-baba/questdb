/*
 * Copyright (c) 2014. Vlad Ilyushchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nfsdb.ql.ops;

import com.nfsdb.ql.Record;
import com.nfsdb.ql.SymFacade;
import com.nfsdb.ql.parser.ParserException;
import com.nfsdb.storage.ColumnType;

public class DoubleScaledEqualsOperator extends AbstractVirtualColumn implements Function {

    private VirtualColumn lhs;
    private VirtualColumn rhs;
    private VirtualColumn scale;

    public DoubleScaledEqualsOperator() {
        super(ColumnType.BOOLEAN);
    }

    @Override
    public boolean getBool(Record rec) {
        double d = lhs.getDouble(rec) - rhs.getDouble(rec);
        return d > 0 ? d < this.scale.getDouble(rec) : d > -this.scale.getDouble(rec);
    }

    @Override
    public boolean isConstant() {
        return lhs.isConstant() && rhs.isConstant() && scale.isConstant();
    }

    @Override
    public void prepare(SymFacade facade) {
        lhs.prepare(facade);
        rhs.prepare(facade);
        scale.prepare(facade);
    }

    @Override
    public void setArg(int pos, VirtualColumn arg) throws ParserException {
        switch (pos) {
            case 0:
                lhs = arg;
                break;
            case 1:
                rhs = arg;
                break;
            default:
                scale = arg;
        }
    }

    @Override
    public void setArgCount(int count) {

    }
}
