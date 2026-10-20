import { useEffect, useState } from 'react'
import { api } from './api'
import { DIRECTORIES } from './directories'

const emptyForm = def =>
  Object.fromEntries(def.fields.map(f => [f.key, '']))

function toBody(def, form) {
  const body = {}
  for (const f of def.fields) {
    const v = form[f.key]
    if (v === '' || v === undefined) body[f.key] = null
    else if (f.type === 'number' || f.type === 'ref') body[f.key] = Number(v)
    else body[f.key] = v
  }
  return body
}

export default function Directory({ name }) {
  const def = DIRECTORIES[name]
  const [items, setItems] = useState([])
  const [refs, setRefs] = useState({})
  const [editing, setEditing] = useState(undefined)
  const [form, setForm] = useState({})
  const [errors, setErrors] = useState({})
  const [deleting, setDeleting] = useState(null)
  const [replaceWith, setReplaceWith] = useState('')
  const [deleteError, setDeleteError] = useState(null)

  const load = () => api(def.url).then(setItems)

  useEffect(() => {
    setEditing(undefined); setDeleting(null)
    load()
    def.fields.filter(f => f.type === 'ref').forEach(f =>
      api(DIRECTORIES[f.ref].url).then(list => setRefs(r => ({ ...r, [f.ref]: list }))))
  }, [name])

  function openForm(item) {
    setEditing(item)
    setForm(item ? def.toForm(item) : emptyForm(def))
    setErrors({})
  }

  async function save(e) {
    e.preventDefault()
    try {
      await api(editing ? `${def.url}/${editing.id}` : def.url, {
        method: editing ? 'PUT' : 'POST',
        body: JSON.stringify(toBody(def, form)),
      })
      setEditing(undefined)
      load()
    } catch (err) {
      setErrors(err)
    }
  }

  function startDelete(item) {
    setDeleting(item); setReplaceWith(''); setDeleteError(null)
  }

  async function confirmDelete() {
    const query = replaceWith ? `?replaceWith=${replaceWith}` : ''
    try {
      await api(`${def.url}/${deleting.id}${query}`, { method: 'DELETE' })
      setDeleting(null)
      load()
    } catch (err) {
      setDeleteError(err.error ?? 'не удалось удалить')
    }
  }

  const overlay = { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
                    display: 'flex', alignItems: 'center', justifyContent: 'center' }
  const box = { background: 'white', color: 'black', padding: 20, minWidth: 320 }

  return (
    <div>
      <h1>{def.title}</h1>
      <button onClick={() => openForm(null)}>создать</button>

      <table border="1" cellPadding="4" style={{ marginTop: 8 }}>
        <thead>
          <tr>{def.columns.map(c => <th key={c.title}>{c.title}</th>)}<th>действия</th></tr>
        </thead>
        <tbody>
          {items.map(item => (
            <tr key={item.id}>
              {def.columns.map(c => <td key={c.title}>{c.value(item)}</td>)}
              <td>
                <button onClick={() => openForm(item)}>изменить</button>{' '}
                <button onClick={() => startDelete(item)}>удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {editing !== undefined && (
        <div style={overlay}>
          <form onSubmit={save} style={box}>
            <h2>{editing ? `изменить #${editing.id}` : 'создать'}</h2>
            {errors.error && <div style={{ color: 'red' }}>{errors.error}</div>}
            {def.fields.map(f => (
              <div key={f.key} style={{ marginBottom: 8 }}>
                <label>{f.label}<br />
                  {f.type === 'select' ? (
                    <select value={form[f.key]} onChange={e => setForm({ ...form, [f.key]: e.target.value })}>
                      <option value="">— нет —</option>
                      {f.options.map(o => <option key={o} value={o}>{o}</option>)}
                    </select>
                  ) : f.type === 'ref' ? (
                    <select value={form[f.key]} onChange={e => setForm({ ...form, [f.key]: e.target.value })}>
                      <option value="">— нет —</option>
                      {(refs[f.ref] ?? []).map(o =>
                        <option key={o.id} value={o.id}>{DIRECTORIES[f.ref].label(o)}</option>)}
                    </select>
                  ) : (
                    <input type={f.type} value={form[f.key]}
                           onChange={e => setForm({ ...form, [f.key]: e.target.value })} />
                  )}
                </label>
                {errors[f.key] && <div style={{ color: 'red' }}>{errors[f.key]}</div>}
              </div>
            ))}
            <button type="submit">сохранить</button>{' '}
            <button type="button" onClick={() => setEditing(undefined)}>отмена</button>
          </form>
        </div>
      )}

      {deleting && (
        <div style={overlay}>
          <div style={box}>
            <h2>удалить #{deleting.id}</h2>
            <p>
              если этот объект связан с другими, выбери, чем его заменить
              {def.nullableReplace && ' можно оставить пустым '}
            </p>
            <select value={replaceWith} onChange={e => setReplaceWith(e.target.value)}>
              <option value="">— без замены —</option>
              {items.filter(i => i.id !== deleting.id).map(i =>
                <option key={i.id} value={i.id}>{def.label(i)}</option>)}
            </select>
            {deleteError && <div style={{ color: 'red', marginTop: 8 }}>{deleteError}</div>}
            <p>
              <button onClick={confirmDelete}>удалить</button>{' '}
              <button onClick={() => setDeleting(null)}>отмена</button>
            </p>
          </div>
        </div>
      )}
    </div>
  )
}