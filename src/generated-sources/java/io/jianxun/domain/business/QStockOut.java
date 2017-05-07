package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStockOut is a Querydsl query type for StockOut
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStockOut extends EntityPathBase<StockOut> {

    private static final long serialVersionUID = 1855220205L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStockOut stockOut = new QStockOut("stockOut");

    public final io.jianxun.domain.QAbstractBusinessDepartEntity _super;

    //inherited
    public final BooleanPath active;

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    // inherited
    public final QDepart depart;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath remark = createString("remark");

    public final StringPath requisitionUser = createString("requisitionUser");

    public final QSparePart sparePart;

    //inherited
    public final NumberPath<Long> version;

    public QStockOut(String variable) {
        this(StockOut.class, forVariable(variable), INITS);
    }

    public QStockOut(Path<? extends StockOut> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStockOut(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStockOut(PathMetadata metadata, PathInits inits) {
        this(StockOut.class, metadata, inits);
    }

    public QStockOut(Class<? extends StockOut> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessDepartEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = _super.depart;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.sparePart = inits.isInitialized("sparePart") ? new QSparePart(forProperty("sparePart"), inits.get("sparePart")) : null;
        this.version = _super.version;
    }

}

