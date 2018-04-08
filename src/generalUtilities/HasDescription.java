package generalUtilities;

public interface HasDescription {

    String provideDescription();

    default void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append(provideDescription());
    }

}
