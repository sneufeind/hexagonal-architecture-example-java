package archtest;

public enum PackageType {

    APPLICATION_ROOT("..application"),
    APPLICATION_SERVICE("..application.service"),
    APPLICATION_USECASE("..application.usecase"),
    CONFIG_ROOT("..config"),
    DOMAIN_EVENT("..domain.event"),
    DOMAIN_EXCEPTION("..domain.exception"),
    DOMAIN_MODEL("..domain.model"),
    DOMAIN_PORT("..domain.port"),
    DOMAIN_ROOT("..domain"),
    DOMAIN_SERVICE("..domain.service"),
    DOMAIN_SERVICE_IMPL("..domain.service.impl"),
    INFRASTRUCTURE_ROOT("..adapter"),
    INFRASTRUCTURE_ADAPTER("..adapter"),

    JAVA_ROOT("java");

    private final String packageName;

    private PackageType(final String packageName) {
        this.packageName = packageName;
    }

    public String get(){
        return this.packageName;
    }

    public String recursive(){
        return String.format("%s..", this.packageName);
    }
}
