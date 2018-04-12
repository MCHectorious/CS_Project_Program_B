package generalUtilities;

public interface HasDescription {

    String provideDescription();//allows the object to give a textual description of itself

    default void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append(provideDescription());
    }// overloaded with string builder because it is much faster

}
