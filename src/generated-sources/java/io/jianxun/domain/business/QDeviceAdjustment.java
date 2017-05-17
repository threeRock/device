package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceAdjustment is a Querydsl query type for DeviceAdjustment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDeviceAdjustment extends EntityPathBase<DeviceAdjustment> {

    private static final long serialVersionUID = -563963656L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceAdjustment deviceAdjustment = new QDeviceAdjustment("deviceAdjustment");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    public final DatePath<java.time.LocalDate> adjustDate = createDate("adjustDate", java.time.LocalDate.class);

    public final StringPath content = createString("content");

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final StringPath description = createString("description");

    public final QDevice device;

    //inherited
    public final NumberPath<Long> id;

    public final StringPath implementation = createString("implementation");

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Long> version;

    public QDeviceAdjustment(String variable) {
        this(DeviceAdjustment.class, forVariable(variable), INITS);
    }

    public QDeviceAdjustment(Path<? extends DeviceAdjustment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceAdjustment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceAdjustment(PathMetadata metadata, PathInits inits) {
        this(DeviceAdjustment.class, metadata, inits);
    }

    public QDeviceAdjustment(Class<? extends DeviceAdjustment> type, PathMetadata metadata, PathInits inits) {
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

