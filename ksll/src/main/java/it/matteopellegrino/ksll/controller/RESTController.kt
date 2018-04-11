package it.matteopellegrino.ksll.controller

import android.util.Log
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import it.matteopellegrino.ksll.model.RemoteLib
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class RESTController : ApiController{
    override fun retrieveAvailableAPI(url: URL, success: (remoteLibs: List<RemoteLib>) -> Unit, failure: () -> Unit) {
        url.toString().httpGet().responseJson { _, _, result ->
            when(result){
                is Result.Failure -> {
                    Log.e(javaClass.simpleName, "Http request to $url failed")
                    failure()
                }

                is Result.Success -> {
                    Log.d(javaClass.simpleName, "Http request success! Elaborating...")

                    fun jsonToRemoteLib(obj: JSONObject): RemoteLib = RemoteLib(URL(obj.getString("url")),
                            obj.getString("sapclassName"),
                            obj.getString("version"),
                            obj.getString("extension"))


                    try{
                        val jsonArray = result.get().array()
                        val remoteLibs: MutableList<RemoteLib> = mutableListOf()

                        for(i in 0 until jsonArray.length()){
                            val obj = jsonArray.getJSONObject(i)
                            remoteLibs.add(jsonToRemoteLib(obj))
                        }

                        success(remoteLibs)
                        return@responseJson
                    }catch (ignored: JSONException){}

                    try{
                        val obj =  result.get().obj()
                        success(listOf(jsonToRemoteLib(obj)))
                        return@responseJson
                    }catch (ignored: JSONException){ }

                    failure()
                }
            }
        }
    }
}