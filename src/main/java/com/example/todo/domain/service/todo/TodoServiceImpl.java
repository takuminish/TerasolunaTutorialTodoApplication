package com.example.todo.domain.service.todo;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoRepositoryImpl;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

	private static final long MAX_UNFINISHED_COUNT = 5;
	
	@Inject
	private TodoRepositoryImpl todoRepositoryImpl;
	
	private Todo findOne(String todoId) {
		Todo todo = todoRepositoryImpl.findById(todoId).orElse(null);
		
		if (todo == null) {
			ResultMessages messages = ResultMessages.error();
			messages.add(ResultMessage
					.fromText("[E404] The requested Todo is not found. (id=" + todoId + ""));
			
			throw new ResourceNotFoundException(messages);
		}
		
		return todo;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<Todo> findAll() {
		return todoRepositoryImpl.findAll();
	}

	@Override
	public Todo create(Todo todo) {
		long unfinishedCount = todoRepositoryImpl.countByFinished(false);
		if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage
                    .fromText("[E001] The count of un-finished Todo must not be over "
                            + MAX_UNFINISHED_COUNT + "."));
		}
		
		
		todo.setTodoId(UUID.randomUUID().toString());
		todo.setCreatedAt(new Date());
		todo.setFinished(false);
		todoRepositoryImpl.create(todo);
		return todo;
	}

	@Override
	public Todo finish(String todoId) {
		Todo todo = findOne(todoId);
		
        if (todo.isFinished()) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage
                    .fromText("[E002] The requested Todo is already finished. (id="
                            + todoId + ")"));
            throw new BusinessException(messages);
        }
		
		todo.setFinished(true);
		todoRepositoryImpl.updateById(todo);
		
		return todo;
	}

	@Override
	public void delete(String todoId) {
		Todo todo = findOne(todoId);
		todoRepositoryImpl.deleteById(todo);

	}

}
