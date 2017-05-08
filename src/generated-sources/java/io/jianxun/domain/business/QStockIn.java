package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStockIn is a Querydsl query type for StockIn
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStockIn extends EntityPathBase<StockIn> {

    private static final long serialVersionUID = 1860960934L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStockIn stockIn = new QStockIn("stockIn");

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

    public final QSparePart sparepart;

    //inherited
    public final NumberPath<Long> version;

    public QStockIn(String variable) {
        this(StockIn.class, forVariable(variable), INITS);
    }

    public QStockIn(Path<? extends StockIn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStockIn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStockIn(PathMetadata metadata, PathInits inits) {
        this(StockIn.class, metadata, inits);
    }

    public QStockIn(Class<? extends StockIn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessDepartEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = _super.depart;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.sparepart = inits.isInitialized("sparepart") ? new QSparePart(forProperty("sparepart"), inits.get("sparepart")) : null;
        this.version = _super.version;
    }

}

