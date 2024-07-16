pub mod app;
pub mod database;

use std::sync::OnceLock;
use wasm_bindgen::prelude::wasm_bindgen;

pub use crux_core::{bridge::Bridge, Core, Request};

pub use app::*;


uniffi::include_scaffolding!("shared");

fn core() -> &'static Bridge<Effect, Counter> {
    static CORE: OnceLock<Bridge<Effect, Counter>> = OnceLock::new();
    CORE.get_or_init(|| Bridge::new(Core::new()))
}

#[wasm_bindgen]
pub fn process_event(data: &[u8]) -> Vec<u8> {
    core().process_event(data)
}

#[wasm_bindgen]
pub fn handle_response(id: u32, data: &[u8]) -> Vec<u8> {
    core().handle_response(id, data)
}

#[wasm_bindgen]
pub fn view() -> Vec<u8> {
    core().view()
}
