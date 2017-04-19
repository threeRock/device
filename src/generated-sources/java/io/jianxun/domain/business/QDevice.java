package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDevice is a Querydsl query type for Device
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDevice extends EntityPathBase<Device> {

    private static final long serialVersionUID = 1418070315L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDevice device = new QDevice("device");

    public final io.jianxun.domain.QAbstractBusinessDepartEntity _super;

    //inherited
    public final BooleanPath active;

    public final StringPath code = createString("code");

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final StringPath dateInfo = createString("dateInfo");

    // inherited
    public final QDepart depart;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath mainPic = createString("mainPic");

    public final QSparePartMainType mainType;

    public final StringPath manufacturer = createString("manufacturer");

    public final StringPath name = createString("name");

    public final ListPath<String, StringPath> pics = this.<String, StringPath>createList("pics", String.class, StringPath.class, PathInits.DIRECT2);

    public final QProductionLine productionLine;

    public final StringPath serialNumber = createString("serialNumber");

    public final StringPath status = createString("status");

    public final StringPath typeInfo = createString("typeInfo");

    //inherited
    public final NumberPath<Long> version;

    public final StringPath weightInfo = createString("weightInfo");

    public QDevice(String variable) {
        this(Device.class, forVariable(variable), INITS);
    }

    public QDevice(Path<? extends Device> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDevice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDevice(PathMetadata metadata, PathInits inits) {
        this(Device.class, metadata, inits);
    }

    public QDevice(Class<? extends Device> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessDepartEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = _super.depart;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.mainType = inits.isInitialized("mainType") ? new QSparePartMainType(forProperty("mainType"), inits.get("mainType")) : null;
        this.productionLine = inits.isInitialized("productionLine") ? new QProductionLine(forProperty("productionLine"), inits.get("productionLine")) : null;
        this.version = _super.version;
    }

}

