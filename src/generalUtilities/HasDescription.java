package generalUtilities;

public interface HasDescription {

    String description();

    default void description(StringBuilder stringBuilder) {
        stringBuilder.append(description());
    }

}
