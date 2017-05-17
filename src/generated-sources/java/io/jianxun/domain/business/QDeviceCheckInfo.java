package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceCheckInfo is a Querydsl query type for DeviceCheckInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDeviceCheckInfo extends EntityPathBase<DeviceCheckInfo> {

    private static final long serialVersionUID = -1900465941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceCheckInfo deviceCheckInfo = new QDeviceCheckInfo("deviceCheckInfo");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    public final DatePath<java.time.LocalDate> checkDate = createDate("checkDate", java.time.LocalDate.class);

    public final StringPath content = createString("content");

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final StringPath description = createString("description");

    public final QDevice device;

    public final StringPath evaluation = createString("evaluation");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath implementation = createString("implementation");

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath nature = createString("nature");

    public final StringPath projectCode = createString("projectCode");

    public final StringPath replacement = createString("replacement");

    //inherited
    public final NumberPath<Long> version;

    public QDeviceCheckInfo(String variable) {
        this(DeviceCheckInfo.class, forVariable(variable), INITS);
    }

    public QDeviceCheckInfo(Path<? extends DeviceCheckInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceCheckInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceCheckInfo(PathMetadata metadata, PathInits inits) {
        this(DeviceCheckInfo.class, metadata, inits);
    }

    public QDeviceCheckInfo(Class<? extends DeviceCheckInfo> type, PathMetadata metadata, PathInits inits) {
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

