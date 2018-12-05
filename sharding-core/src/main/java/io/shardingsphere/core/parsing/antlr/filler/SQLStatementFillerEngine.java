/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antlr.filler;

import com.google.common.base.Optional;
import io.shardingsphere.core.constant.DatabaseType;
import io.shardingsphere.core.metadata.table.ShardingTableMetaData;
import io.shardingsphere.core.parsing.antlr.ast.SQLStatementType;
import io.shardingsphere.core.parsing.antlr.sql.segment.SQLSegment;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import io.shardingsphere.core.rule.ShardingRule;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * SQL statement filler engine.
 * 
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class SQLStatementFillerEngine {
    
    private final DatabaseType databaseType;
    
    private final String sql;
    
    private final ShardingRule shardingRule;
    
    private final ShardingTableMetaData shardingTableMetaData;
    
    /**
     * Fill SQL statement.
     *
     * @param sqlSegments SQL segments
     * @param sqlStatementType SQL statement type
     * @return SQL statement
     */
    public SQLStatement fill(final Collection<SQLSegment> sqlSegments, final SQLStatementType sqlStatementType) {
        SQLStatement result = SQLStatementFactory.getInstance(databaseType, sqlStatementType);
        for (SQLSegment each : sqlSegments) {
            Optional<SQLSegmentFiller> filler = SQLSegmentFillerRegistry.findFiller(each);
            if (filler.isPresent()) {
                filler.get().fill(each, result, sql, shardingRule, shardingTableMetaData);
            }
        }
        return result;
    }
}