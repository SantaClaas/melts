pub mod app;
pub mod database;

use std::path::Path;
use std::sync::OnceLock;

pub use app::*;


uniffi::include_scaffolding!("shared");

struct Counter {
    count: isize,
}

fn core() -> &'static Counter {
    static CORE: OnceLock<Counter> = OnceLock::new();
    CORE.get_or_init(|| Counter { count: 0 })
}

fn increment() -> i32 {
    3
}

async fn open_database(path: String) -> String {
    let database_path = Path::new(&path).join("test.db");
    // let result = libsql::Builder::new_local(database_path).build().await;
    // rusqlite::
    // match result {
    //     Ok(databse) => path,
    //     Err(error) => error.to_string(),
    // }


    let is_created = database_path.exists();

    let connection =
        rusqlite::Connection::open(path);

    let Ok(connection) = connection else {
        return "Error opening database".to_string();
    };
    // Set encryption key for sqlcipher
    let result = connection
        .pragma_update(None, "key", "TODO");

    if result.is_err() {
        return "Error setting key".to_string();
    }


    // Enable foreign keys
    // connection
    //     .set_db_config(DbConfig::SQLITE_DBCONFIG_ENABLE_FKEY, true)
    //     .map_err(OpenDatabaseError::EnableForeignKeysError)?;
    //
    // if !is_created {
    //     create_tables(&connection).map_err(OpenDatabaseError::CreateTablesError)?;
    // }

    return "Success".to_string();
}