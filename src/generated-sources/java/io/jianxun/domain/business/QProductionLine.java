package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductionLine is a Querydsl query type for ProductionLine
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductionLine extends EntityPathBase<ProductionLine> {

    private static final long serialVersionUID = -547793374L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductionLine productionLine = new QProductionLine("productionLine");

    public final io.jianxun.domain.QAbstractBusinessDepartEntity _super;

    //inherited
    public final BooleanPath active;

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

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Long> version;

    public QProductionLine(String variable) {
        this(ProductionLine.class, forVariable(variable), INITS);
    }

    public QProductionLine(Path<? extends ProductionLine> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductionLine(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductionLine(PathMetadata metadata, PathInits inits) {
        this(ProductionLine.class, metadata, inits);
    }

    public QProductionLine(Class<? extends ProductionLine> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessDepartEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = _super.depart;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

