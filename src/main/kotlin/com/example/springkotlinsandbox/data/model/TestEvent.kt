package com.example.springkotlinsandbox.data.model

import org.springframework.context.ApplicationEvent

class TestEvent(source: Any, val data: String) : ApplicationEvent(source) {


}
