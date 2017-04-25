package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsertest is a Querydsl query type for Usertest
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUsertest extends EntityPathBase<Usertest> {

    private static final long serialVersionUID = -113779598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsertest usertest = new QUsertest("usertest");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final QDepttest depttest;

    public final StringPath displayName = createString("displayName");

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    //inherited
    public final NumberPath<Long> version;

    public QUsertest(String variable) {
        this(Usertest.class, forVariable(variable), INITS);
    }

    public QUsertest(Path<? extends Usertest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsertest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsertest(PathMetadata metadata, PathInits inits) {
        this(Usertest.class, metadata, inits);
    }

    public QUsertest(Class<? extends Usertest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depttest = inits.isInitialized("depttest") ? new QDepttest(forProperty("depttest"), inits.get("depttest")) : null;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

