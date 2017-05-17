package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceFault is a Querydsl query type for DeviceFault
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDeviceFault extends EntityPathBase<DeviceFault> {

    private static final long serialVersionUID = 943735735L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceFault deviceFault = new QDeviceFault("deviceFault");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final StringPath description = createString("description");

    public final QDevice device;

    public final StringPath duration = createString("duration");

    public final StringPath effect = createString("effect");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath implementation = createString("implementation");

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath measures = createString("measures");

    public final StringPath nature = createString("nature");

    public final DatePath<java.time.LocalDate> occurrenceDate = createDate("occurrenceDate", java.time.LocalDate.class);

    public final StringPath reason = createString("reason");

    //inherited
    public final NumberPath<Long> version;

    public QDeviceFault(String variable) {
        this(DeviceFault.class, forVariable(variable), INITS);
    }

    public QDeviceFault(Path<? extends DeviceFault> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceFault(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceFault(PathMetadata metadata, PathInits inits) {
        this(DeviceFault.class, metadata, inits);
    }

    public QDeviceFault(Class<? extends DeviceFault> type, PathMetadata metadata, PathInits inits) {
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

