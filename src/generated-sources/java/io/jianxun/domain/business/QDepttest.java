package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDepttest is a Querydsl query type for Depttest
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDepttest extends EntityPathBase<Depttest> {

    private static final long serialVersionUID = 1099833836L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepttest depttest = new QDepttest("depttest");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath levelCode = createString("levelCode");

    public final StringPath name = createString("name");

    public final QDepttest parent;

    //inherited
    public final NumberPath<Long> version;

    public QDepttest(String variable) {
        this(Depttest.class, forVariable(variable), INITS);
    }

    public QDepttest(Path<? extends Depttest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDepttest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDepttest(PathMetadata metadata, PathInits inits) {
        this(Depttest.class, metadata, inits);
    }

    public QDepttest(Class<? extends Depttest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.parent = inits.isInitialized("parent") ? new QDepttest(forProperty("parent"), inits.get("parent")) : null;
        this.version = _super.version;
    }

}

