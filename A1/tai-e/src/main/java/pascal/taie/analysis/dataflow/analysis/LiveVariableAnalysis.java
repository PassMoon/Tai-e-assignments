/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2022 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2022 Yue Li <yueli@nju.edu.cn>
 *
 * This file is part of Tai-e.
 *
 * Tai-e is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Tai-e is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Tai-e. If not, see <https://www.gnu.org/licenses/>.
 */

package pascal.taie.analysis.dataflow.analysis;

import pascal.taie.analysis.dataflow.fact.SetFact;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.exp.RValue;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.Stmt;

/**
 * Implementation of classic live variable analysis.
 */
public class LiveVariableAnalysis extends
        AbstractDataflowAnalysis<Stmt, SetFact<Var>> {

    public static final String ID = "livevar";

    public LiveVariableAnalysis(AnalysisConfig config) {
        super(config);
    }

    @Override
    public boolean isForward() {
        return false;
    }

    @Override
    public SetFact<Var> newBoundaryFact(CFG<Stmt> cfg) {
        // TODO - finish me
        // 返回边界条件全为0
        return new SetFact<>();
    }

    @Override
    public SetFact<Var> newInitialFact() {
        // TODO - finish me
        // 初始化所有基本块输入为0
        return new SetFact<>();
    }

    @Override
    public void meetInto(SetFact<Var> fact, SetFact<Var> target) {
        // TODO - finish me
        // 合并所有后继基本块输入
        target.union(fact);
    }

    @Override
    public boolean transferNode(Stmt stmt, SetFact<Var> in, SetFact<Var> out) {
        // TODO - finish me
        // 判断基本块中语句，使用了变1，重新定义变0
        // 修改in，所以out不变

        SetFact<Var> Intemp = new SetFact<>();
        Intemp.union(out);

        if(stmt.getDef().isPresent()){
            if(stmt.getDef().get() instanceof Var){
                Intemp.remove((Var)stmt.getDef().get());
            }
        }

        for(RValue r : stmt.getUses()){
            if(r instanceof Var){
                Intemp.add((Var)r);
            }
        }

        if (!in.equals(Intemp)) {
            in.set(Intemp); // 把新值设置进去
            return true;
        } else {
            return false;
        }

    }
}
