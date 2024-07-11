package ru.cft.template.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Context {
    private static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(Context::new);
    private Session session;
    private User user;

    public static Context get() {
        return CONTEXT.get();
    }
}
