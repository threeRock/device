package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceDiscard is a Querydsl query type for DeviceDiscard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDeviceDiscard extends EntityPathBase<DeviceDiscard> {

    private static final long serialVersionUID = -856162061L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceDiscard deviceDiscard = new QDeviceDiscard("deviceDiscard");

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

    public final StringPath reason = createString("reason");

    //inherited
    public final NumberPath<Long> version;

    public QDeviceDiscard(String variable) {
        this(DeviceDiscard.class, forVariable(variable), INITS);
    }

    public QDeviceDiscard(Path<? extends DeviceDiscard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceDiscard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceDiscard(PathMetadata metadata, PathInits inits) {
        this(DeviceDiscard.class, metadata, inits);
    }

    public QDeviceDiscard(Class<? extends DeviceDiscard> type, PathMetadata metadata, PathInits inits) {
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

