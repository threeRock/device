package io.jianxun.domain.business.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1301291687L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final io.jianxun.domain.business.QAbstractBusinessEntity _super;

    public final BooleanPath accountNonExpired = createBoolean("accountNonExpired");

    public final BooleanPath accountNonLocked = createBoolean("accountNonLocked");

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final BooleanPath credentialsNonExpired = createBoolean("credentialsNonExpired");

    public final StringPath displayName = createString("displayName");

    public final BooleanPath enabled = createBoolean("enabled");

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath password = createString("password");

    public final ListPath<io.jianxun.domain.business.role.Role, io.jianxun.domain.business.role.QRole> roles = this.<io.jianxun.domain.business.role.Role, io.jianxun.domain.business.role.QRole>createList("roles", io.jianxun.domain.business.role.Role.class, io.jianxun.domain.business.role.QRole.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    //inherited
    public final NumberPath<Long> version;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.business.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

