package infinum.academy2019.shows_danijel_pecek.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.Comment
import infinum.academy2019.shows_danijel_pecek.data.model.MessageBody
import infinum.academy2019.shows_danijel_pecek.data.model.MessageModel
import infinum.academy2019.shows_danijel_pecek.data.model.MessageResponse
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class CommentsViewModel : ViewModel() {

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }

    var commentResponseLiveData: LiveData<MessageResponse>? = null


    var commentsLiveData: LiveData<List<Comment>>? = null

    fun getComments(episodeId: String) {
        Repository.getComments(episodeId)
        commentsLiveData = Repository.liveDataComments()
    }


    fun postComment(comment: MessageModel){
        Repository.postComment(comment)
    }
}