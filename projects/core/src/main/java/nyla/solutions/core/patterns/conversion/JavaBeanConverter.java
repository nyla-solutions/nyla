package nyla.solutions.core.patterns.conversion;

import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.JavaBean;

/**
 * @author Gregory Green
 */
public class JavaBeanConverter<SourceType, TargetType> implements Converter<SourceType, TargetType>{
    private final Class<TargetType> targetClass;

    public JavaBeanConverter(Class<TargetType> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public TargetType convert(SourceType sourceObject) {
        //Create target instance

        if(targetClass.isRecord())
        {
            var recordComponents = targetClass.getRecordComponents();
            Object[] inputArgs = new Object[recordComponents.length];

            for (int i = 0; i < recordComponents.length; i++) {
                var component = recordComponents[i];
                inputArgs[i] = JavaBean.getProperty(sourceObject, component.getName());
            }

            return ClassPath.newInstance(targetClass,inputArgs);
        }

        TargetType target = ClassPath.newInstance(targetClass);
        JavaBean.populate(JavaBean.toMap(sourceObject), target);

        return target;
    }
}
