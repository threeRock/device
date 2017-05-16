package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceTechnicalParam is a Querydsl query type for DeviceTechnicalParam
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDeviceTechnicalParam extends EntityPathBase<DeviceTechnicalParam> {

    private static final long serialVersionUID = 696379707L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceTechnicalParam deviceTechnicalParam = new QDeviceTechnicalParam("deviceTechnicalParam");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final QDevice device;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath name = createString("name");

    public final StringPath params = createString("params");

    //inherited
    public final NumberPath<Long> version;

    public QDeviceTechnicalParam(String variable) {
        this(DeviceTechnicalParam.class, forVariable(variable), INITS);
    }

    public QDeviceTechnicalParam(Path<? extends DeviceTechnicalParam> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceTechnicalParam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceTechnicalParam(PathMetadata metadata, PathInits inits) {
        this(DeviceTechnicalParam.class, metadata, inits);
    }

    public QDeviceTechnicalParam(Class<? extends DeviceTechnicalParam> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.device = inits.isInitialized("device") ? new QDevice(forProperty("device"), inits.get("device")) : null;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

