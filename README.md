# Kotlin Safety Library Loader

Dynamically download libraries in Kotlin/Android with digital signature checking at runtime.

## Why ? 

This is a thesis project at University of Bologna, Italy. 
The idea behind is to prevent personal user data spreading to third-party cloud systems, 
maintaining the feature to provide multiple server-side implementation of a service.
With this Android library you can download at runtime specific `dex` libraries and
retrieve its functionality.

## Usage

The functionality depends on metadata exposed by a server.
A built-in JSON RESTful server support is ready to use.
You can provide a different implementation. See [ServerManager](https://github.com/arabello/KSLL/blob/master/ksll/src/main/java/it/matteopellegrino/ksll/servermanager/ServerManager.kt) for details.
 
The server endpoint, for example `http://example.org:8080`, must returns specific metadata, such as

``` Kotlin
{
    "url": "http://example.org:8080/path/to/binary",
    "sapclassName": "your.sap.class.Name",
    "version": "0.0.1",
    "extension": "dex",
    "signature": "DdglDaYEz1ApDmO...df/ydQ1SM=",
    "publicKey":"MIGfMA0GCSq...2+AgtGQIDAQAB"
}
```

The Service Access Point Class Name (`sapclassName`) is the name of the class inside the library 
that will be read using reflection. This class methods represent all available functionality of the library.


To download and use the library exposed by the server

``` Kotlin
val ksll = Ksll(baseContext, RESTManager())

ksll.load("http://example.org:8080", { remoteLib ->
	remoteLib.require{ obj, methods ->
		methods.forEach{ println(it) }
	}
}, { error ->
	println("Error: $error")	
})

```

Also using extension function

``` Kotlin
val ksll = Ksll(baseContext, RESTManager())

"http://example.org:8080".load(ksll, { remoteLib ->
	remoteLib.require{ obj, methods ->
		methods.forEach{ println(it) }
	}
}, { error ->
	println("Error: $error")	
})

```

Callbacks

``` Kotlin
success: (lib: Lib) -> Unit
failure: (cause: Failure) -> Unit
```

Handle different types of error

``` Kotlin
﻿ksll.load("http://192.168.1.150:8080", ... , { error ->
        val msg = when(error){
            Failure.NotTrustedData -> "Signature verification failed. Library not trusted."
            Failure.HTTPRequestError -> "Connection problem. Cannot retrieve library."
            else -> {
                "Unexpected error. Cannot load library."
            }
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
})
```

Once a library `RemoteLib`/`Lib` is retrieved, to load the invokable methods

``` Kotlin
remoteLib.require(ksll){ obj, methods ->

}

lib.require(ksll){ obj, methods ->

}
```

## Sample

See the [sample application](https://github.com/arabello/KSLL/tree/master/sample) for detailed usage cases.

## Licenses

KSLL is released under the [MIT](http://opensource.org/licenses/MIT) license.