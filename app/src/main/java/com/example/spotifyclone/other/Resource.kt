package com.example.spotifyclone.other

 data class Resource<out T>(val state: state,val data:T?,var error:String?) {
     companion object{
         fun <T> success(data:T?)=Resource(state.state_success,data,null)
         fun<T> error(data:T?,error: String)=Resource(state.state_error,null, error)
         fun<T> loading(data:T?)= Resource(state.state_loading,data,null)
     }
}
enum class state{
    state_loading,
    state_success,
    state_error
}