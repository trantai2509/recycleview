package vn.ute.udn.recyclevieww.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.ute.udn.recyclevieww.databinding.ItemStudentBinding
import vn.ute.udn.recyclevieww.model.Student

class StudentListAdapter(
    private val onStudentClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentListAdapter.StudentViewHolder>(StudentDiffCallback()) {

    companion object {
        const val PAYLOAD_GPA_CHANGED = "PAYLOAD_GPA_CHANGED"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.contains(PAYLOAD_GPA_CHANGED)) {
            holder.updateGpaOnly(getItem(position).gpa)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class StudentViewHolder(
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.tvName.text = student.name
            binding.tvMssv.text = "MSSV: ${student.mssv}"
            binding.tvGpa.text = "GPA: ${student.gpa}"
            binding.tvAvatar.text = student.name.take(1)
            binding.tvAvatar.background.mutate().setTint(student.avatarColor)

            binding.root.setOnClickListener { onStudentClick(student) }
            binding.btnDelete.setOnClickListener { onDeleteClick(student) }
        }

        fun updateGpaOnly(gpa: Double) {
            binding.tvGpa.text = "GPA: $gpa"
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Student, newItem: Student): Any? {
            return if (oldItem.gpa != newItem.gpa &&
                oldItem.mssv == newItem.mssv &&
                oldItem.name == newItem.name &&
                oldItem.major == newItem.major &&
                oldItem.avatarColor == newItem.avatarColor
            ) {
                PAYLOAD_GPA_CHANGED
            } else {
                super.getChangePayload(oldItem, newItem)
            }
        }
    }
}
