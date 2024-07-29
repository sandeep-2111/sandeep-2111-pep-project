package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.MessageDAO;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     * 
     */
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();
        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO);
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);
        //app.start(8080);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void registerAccount(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Account a=mapper.readValue(ctx.body(),Account.class);
        Account addedAccount=accountService.registerAccount(a);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            ctx.status(400);
        }
    

    }
    private void login(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Account a=mapper.readValue(ctx.body(),Account.class);
        Account identified=accountService.login(a.getUsername(),a.getPassword());
        if(identified!=null){
            ctx.json(identified);
        }
        else{
            ctx.status(401);
        }
    }
    private void createMessage(Context ctx) throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        Message a=mapper.readValue(ctx.body(),Message.class);
        Message created=messageService.createMessage(a);
        if(created!=null){
            ctx.json(created);
        }
        else{
            ctx.status(400);//result();
        }
    }
    private void getAllMessages(Context ctx) throws JsonMappingException, JsonProcessingException {
        //ObjectMapper mapper=new ObjectMapper();
        //Message a=mapper.readValue(ctx.body(),Message.class);
        List<Message> l=messageService.getAllMessages();
        ctx.json(l);
    }
    private void getMessageById(Context ctx) throws JsonMappingException, JsonProcessingException {
        //ObjectMapper mapper=new ObjectMapper();
        //Message a=mapper.readValue(ctx.body(),Message.class);
        int id=Integer.parseInt(ctx.pathParam("message_id"));
        Message m=messageService.getMessageById(id);
        if(m!=null){
        ctx.json(m);
        }
        else{
            ctx.result("");
        }

    }
    private void deleteMessage(Context ctx) throws JsonMappingException, JsonProcessingException {
        //ObjectMapper mapper=new ObjectMapper();
        //Message a=mapper.readValue(ctx.body(),Message.class);
        int message_id=Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted=messageService.getMessageById(message_id);
        if(deleted!=null){
            boolean b=messageService.deleteMessage(message_id);
            if(b){
              ctx.status(200).json(deleted);
            }
           
            else{
               ctx.status(500);
            }

        }
        else{
            ctx.status(200);
            ctx.result("");
        }
    

    }
    private void updateMessageText(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Message a=mapper.readValue(ctx.body(),Message.class);
        int i=Integer.parseInt(ctx.pathParam("message_id"));
        Message existingMessage = messageService.updateMessageText(i, a.getMessage_text()); 
        if(existingMessage == null || existingMessage.message_text.isBlank())
        {

            ctx.status(400); 
        }
        else
        {

           ctx.json(existingMessage); 
        }



    }
    private void getMessagesByUserId(Context ctx) throws JsonMappingException, JsonProcessingException {
        //ObjectMapper mapper=new ObjectMapper();
        //Message a=mapper.readValue(ctx.body(),Message.class);
        int message_id=Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> l=messageService.getMessagesByUserId(message_id);
        ctx.json(l); 
        
        
    }


}