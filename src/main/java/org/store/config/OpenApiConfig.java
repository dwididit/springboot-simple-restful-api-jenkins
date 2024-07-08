package org.store.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(
        name = "ApiKeyAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Store API",
                version = "1.0",
                contact = @Contact(
                        name = "Dwi Didit Prasetiyo", email = "didit@dwidi.dev", url = "https://dwidi.dev"
                ),
                license = @License(
                        name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                description = "This API is created and managed by Dwi Didit Prasetiyo."
        ),
        security = @SecurityRequirement(name = "BearerAuth")
)
public class OpenApiConfig {

}

